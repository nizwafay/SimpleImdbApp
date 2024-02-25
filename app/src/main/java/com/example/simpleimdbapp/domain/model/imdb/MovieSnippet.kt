package com.example.simpleimdbapp.domain.model.imdb

import com.squareup.moshi.Json

data class MovieSnippet(
    val id: Int,
    val title: String,
    @Json(name = "poster_path") val posterPath: String,
)