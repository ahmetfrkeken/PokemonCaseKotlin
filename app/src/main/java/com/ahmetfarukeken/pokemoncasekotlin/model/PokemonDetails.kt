package com.ahmetfarukeken.pokemoncasekotlin.model

import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    @SerializedName("name")
    val name: String,
    @SerializedName("sprites")
    val sprites: PokemonDetailsSprites,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int
)