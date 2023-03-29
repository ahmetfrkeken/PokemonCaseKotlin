package com.ahmetfarukeken.pokemoncasekotlin.view.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetfarukeken.pokemoncasekotlin.R
import com.ahmetfarukeken.pokemoncasekotlin.databinding.MainActivityBinding
import com.ahmetfarukeken.pokemoncasekotlin.model.Pokemon
import com.ahmetfarukeken.pokemoncasekotlin.model.PokemonData
import com.ahmetfarukeken.pokemoncasekotlin.utils.APIConstants
import com.ahmetfarukeken.pokemoncasekotlin.view.activity.pokemon_details.PokemonDetailsActivity
import com.ahmetfarukeken.pokemoncasekotlin.view.activity.request_permission.RequestPermissionActivity
import com.ahmetfarukeken.pokemoncasekotlin.view.adapter.OnRecyclerViewRowClickListener
import com.ahmetfarukeken.pokemoncasekotlin.view.adapter.PokemonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnRecyclerViewRowClickListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainActivityBinding
    private val pokemonRecyclerViewAdapter = PokemonAdapter(arrayListOf(), this)
    private var pokemon: Pokemon? = null
    private var pokemons = ArrayList<PokemonData>()
    private var nextUrl: String? = null
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initRecyclerView()
        observeLiveData()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getOverlayPermissin(this)
    }

    private fun initView() {
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setNavigationItemSelectedListener {
                if (it.itemId == R.id.closeApp) {
                    moveTaskToBack(true);
                    exitProcess(-1)
                }

                true
            }
            errorButton.setOnClickListener {
                pokemons.clear()
                viewModel.getPokemonListFromAPI("${APIConstants.BASE_URL}${APIConstants.POKEMON_END_POINT}")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        viewModel.getPokemonListFromAPI("${APIConstants.BASE_URL}${APIConstants.POKEMON_END_POINT}")
        val recyclerViewLayoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.pokemonListRecyclerView.apply {
            layoutManager = recyclerViewLayoutManager
            adapter = pokemonRecyclerViewAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount: Int = recyclerViewLayoutManager.childCount
                    val pastVisibleItem: Int =
                        recyclerViewLayoutManager.findFirstCompletelyVisibleItemPosition()
                    val total: Int = pokemonRecyclerViewAdapter.itemCount

                    if (this@MainActivity.nextUrl != null) {
                        if (visibleItemCount + pastVisibleItem >= total) {
                            viewModel.getPokemonListFromAPI("$nextUrl")
                        }
                    }
                }
            })
        }
    }

    fun observeLiveData() {
        viewModel.getPokemon().observe(this, Observer { pokemon ->
            pokemon.let {
                this.pokemons.addAll(pokemon.pokemonList)
                this.pokemon = pokemon
                pokemonRecyclerViewAdapter.updatePokemonList(pokemons)
                nextUrl = pokemon.next
            }
        })

        viewModel.isHaveOverlayPermisson().observe(this, Observer {
            if (!it) {
                turnToRequestPermissionScreen()
            }
        })

        viewModel.getPokemonError().observe(this, Observer {
            if (it) {
                binding.apply {
                    errorLinearLayout.visibility = View.VISIBLE
                    pokemonListRecyclerView.visibility = View.GONE
                }
            } else {
                binding.apply {
                    errorLinearLayout.visibility = View.GONE
                    pokemonListRecyclerView.visibility = View.VISIBLE
                }
            }
        })

        viewModel.getPokemonLoading().observe(this, Observer {
            if (it) {
                binding.apply {
                    pokemonProgressBar.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    pokemonProgressBar.visibility = View.GONE
                }
            }
        })
    }

    fun turnToRequestPermissionScreen() {
        val i = Intent(this, RequestPermissionActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    override fun onRowClick(position: Int) {
        val intent = Intent(this@MainActivity, PokemonDetailsActivity::class.java)
        val url = pokemons[position].url ?: ""
        val urlClear = url.substring(0, url.length - 1)
        intent.putExtra("pokemonDetailsUrl", urlClear.substring(urlClear.lastIndexOf('/') + 1))

        startActivity(intent)
    }
}