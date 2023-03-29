package com.ahmetfarukeken.pokemoncasekotlin.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailsSprites(
    @SerializedName("front_default")
    val frontDefault: String,
    @SerializedName("back_default")
    val backDefault: String
)