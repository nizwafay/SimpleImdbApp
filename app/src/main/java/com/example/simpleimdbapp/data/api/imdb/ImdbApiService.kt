package com.example.simpleimdbapp.data.api.imdb

import com.example.simpleimdbapp.domain.model.imdb.Genre
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImdbApiService {
    @GET("genre/movie/list")
    suspend fun getGenres(@Query("language") language: String = "en"): Response<List<Genre>>
}