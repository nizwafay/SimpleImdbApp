package com.example.simpleimdbapp.data.api.imdb

import com.example.simpleimdbapp.domain.model.imdb.Genre
import com.squareup.moshi.Json

data class GetGenresApiResponse(
    @Json(name = "genres") val genres: List<Genre>
)