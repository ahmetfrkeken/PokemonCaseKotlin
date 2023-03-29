package com.ahmetfarukeken.pokemoncasekotlin.service

import com.ahmetfarukeken.pokemoncasekotlin.model.Pokemon
import com.ahmetfarukeken.pokemoncasekotlin.model.PokemonDetails
import com.ahmetfarukeken.pokemoncasekotlin.utils.APIConstants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IPokemonAPI {
    @GET
    fun getPokemons(@Url pokemonsUrl: String): Single<Pokemon>

    @GET
    fun getPokemonDetails(@Url pokemonDetailsUrl: String): Single<PokemonDetails>
}