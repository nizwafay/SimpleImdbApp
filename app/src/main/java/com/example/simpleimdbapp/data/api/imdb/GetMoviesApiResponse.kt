package com.example.simpleimdbapp.data.api.imdb

import com.example.simpleimdbapp.domain.model.imdb.MovieSnippet
import com.squareup.moshi.Json

data class GetMoviesApiResponse(
    val page: Int,
    val results: List<MovieSnippet>,
    @Json(name = "total_pages") val totalPages: Int,
)