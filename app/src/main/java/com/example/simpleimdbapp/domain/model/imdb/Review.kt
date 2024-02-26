package com.example.simpleimdbapp.domain.model.imdb

import com.squareup.moshi.Json

data class Review(
    val author: String,
    val content: String,
    @Json(name = "updated_at") val updatedAt: String,
)
