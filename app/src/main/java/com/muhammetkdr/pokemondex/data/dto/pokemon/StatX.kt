package com.muhammetkdr.pokemondex.data.dto.pokemon


import com.google.gson.annotations.SerializedName

data class StatX(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)