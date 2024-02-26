package com.example.simpleimdbapp.data.api.imdb

import com.example.simpleimdbapp.domain.model.imdb.Genre
import com.squareup.moshi.Json

data class GetMovieDetailApiResponse(
    val id: Int,
    val title: String,
    @Json(name = "release_date") val releaseDate: String,
    val runtime: Int,
    @Json(name = "poster_path") val posterPath: String,
    val overview: String,
    val genres: List<Genre>,
    val video: Boolean,
)