package com.ahmetfarukeken.pokemoncasekotlin.model

import com.google.gson.annotations.SerializedName

data class PokemonData(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)