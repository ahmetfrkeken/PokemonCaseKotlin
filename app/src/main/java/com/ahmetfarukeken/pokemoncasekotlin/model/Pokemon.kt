package com.ahmetfarukeken.pokemoncasekotlin.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val pokemonList: ArrayList<PokemonData>
)