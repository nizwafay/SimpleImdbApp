package com.example.simpleimdbapp.data.api.imdb

import com.example.simpleimdbapp.domain.model.imdb.MovieSnippet

data class GetMoviesApiResponse(
    val page: Int,
    val results: List<MovieSnippet>,
)