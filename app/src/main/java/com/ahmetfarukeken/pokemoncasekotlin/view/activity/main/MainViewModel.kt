package com.ahmetfarukeken.pokemoncasekotlin.view.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmetfarukeken.pokemoncasekotlin.model.Pokemon
import com.ahmetfarukeken.pokemoncasekotlin.service.IPokemonAPI
import com.ahmetfarukeken.pokemoncasekotlin.view.activity.base_view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val iPokemonAPI: IPokemonAPI) : BaseViewModel() {
    private val disposable = CompositeDisposable()

    private val pokemon = MutableLiveData<Pokemon>()
    private val pokemonError = MutableLiveData<Boolean>()
    private val pokemonLoading = MutableLiveData<Boolean>()

    fun getPokemon(): LiveData<Pokemon> {
        return pokemon
    }

    fun getPokemonError(): LiveData<Boolean> {
        return pokemonError
    }

    fun getPokemonLoading(): LiveData<Boolean> {
        return pokemonLoading
    }

    fun getPokemonListFromAPI(pokemonsUrl: String) {
        pokemonLoading.value = true
        pokemonError.value = false

        disposable.add(
            iPokemonAPI.getPokemons(pokemonsUrl = pokemonsUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Pokemon>() {
                    override fun onSuccess(t: Pokemon) {
                        pokemon.value = t
                        pokemonLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        pokemonLoading.value = false
                        pokemonError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}