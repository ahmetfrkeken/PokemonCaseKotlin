package com.ahmetfarukeken.pokemoncasekotlin.view.activity.pokemon_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmetfarukeken.pokemoncasekotlin.model.PokemonDetails
import com.ahmetfarukeken.pokemoncasekotlin.service.IPokemonAPI
import com.ahmetfarukeken.pokemoncasekotlin.service.PokemonAPIService
import com.ahmetfarukeken.pokemoncasekotlin.view.activity.base_view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel
@Inject constructor(private val iPokemonAPI: IPokemonAPI): BaseViewModel() {
    private val pokemonApiService = PokemonAPIService()
    private val disposable = CompositeDisposable()

    private val pokemonDetails = MutableLiveData<PokemonDetails>()
    private val pokemonDetailsError = MutableLiveData<Boolean>()
    private val pokemonDetailsLoading = MutableLiveData<Boolean>()

    fun getPokemonDetails(): LiveData<PokemonDetails> {
        return pokemonDetails
    }

    fun getPokemonDetailsError(): LiveData<Boolean> {
        return pokemonDetailsError
    }

    fun getPokemonDetailsLoading(): LiveData<Boolean> {
        return pokemonDetailsLoading
    }

    fun getPokemonDetailsFromAPI(pokemonDetailsUrl: String) {
        pokemonDetailsLoading.value = true


        disposable.add(
            iPokemonAPI.getPokemonDetails(pokemonDetailsUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PokemonDetails>() {
                    override fun onSuccess(t: PokemonDetails) {
                        pokemonDetails.value = t
                        pokemonDetailsError.value = false
                        pokemonDetailsLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        pokemonDetailsLoading.value = false
                        pokemonDetailsError.value = true
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