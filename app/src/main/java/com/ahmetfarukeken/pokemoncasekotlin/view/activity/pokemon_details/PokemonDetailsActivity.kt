package com.ahmetfarukeken.pokemoncasekotlin.view.activity.pokemon_details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.ahmetfarukeken.pokemoncasekotlin.databinding.PokemonDetailsActivityBinding
import com.ahmetfarukeken.pokemoncasekotlin.model.PokemonDetails
import com.ahmetfarukeken.pokemoncasekotlin.utils.APIConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailsActivity : AppCompatActivity() {
    private lateinit var binding: PokemonDetailsActivityBinding
    private lateinit var viewModel: PokemonDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        observeLiveData()
        getPokemonData()
    }

    private fun initView() {
        binding = PokemonDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PokemonDetailsViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel.getPokemonDetails().observe(this, Observer { pokemonDetails ->
            pokemonDetails?.let {
                updateViewData(pokemonDetails = it)
            }
        })

        viewModel.getPokemonDetailsError().observe(this, Observer { pokemonDetailsError ->
            pokemonDetailsError?.let {
                binding.apply {
                    if (it){
                        pokemonDetailsLinearLayout.visibility = View.GONE
                        errorLinearLayout.visibility = View.VISIBLE
                    }else{
                        pokemonDetailsLinearLayout.visibility = View.VISIBLE
                        errorLinearLayout.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.getPokemonDetailsLoading().observe(this, Observer { pokemonDetailsLoading ->
            pokemonDetailsLoading?.let {

                if (it){
                    binding.pokemonDetailsProgressBar.visibility = View.VISIBLE
                }else{
                    binding.pokemonDetailsProgressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun getPokemonData(){
        val pokemonDetailsUrl = intent.getStringExtra("pokemonDetailsUrl")
        viewModel.getPokemonDetailsFromAPI("${APIConstants.BASE_URL}${APIConstants.POKEMON_END_POINT}/$pokemonDetailsUrl")
    }

    private fun updateViewData(pokemonDetails: PokemonDetails?) {
        binding.apply {
            pokemonImageView.load(pokemonDetails?.sprites?.frontDefault ?: ""){
                crossfade(true)
                crossfade(1000)
            }
            pokemonNameTextField.text = pokemonDetails?.name ?: "-"
            pokemonHeightTextField.text = pokemonDetails?.height?.toString() ?: "-"
            pokemonWeightTextField.text = pokemonDetails?.weight?.toString() ?: "-"
            showPokemonOverlayButton.setOnClickListener {

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}