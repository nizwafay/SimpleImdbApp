package com.example.simpleimdbapp.data.repository.imdb

import com.example.simpleimdbapp.data.api.imdb.ImdbApiService
import com.example.simpleimdbapp.data.repository.BaseRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Genre

class ImdbRepository(private val apiService: ImdbApiService): BaseRepository() {
    suspend fun getGenres(): ApiResponse<List<Genre>> {
        return apiCall { apiService.getGenres() }
    }
}