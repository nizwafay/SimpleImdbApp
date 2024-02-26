package com.example.simpleimdbapp.data.api.imdb

import com.example.simpleimdbapp.domain.model.imdb.Review
import com.squareup.moshi.Json

data class GetMovieReviewsApiResponse(
    val page: Int,
    val results: List<Review>,
    @Json(name = "total_pages") val totalPages: Int,
)
