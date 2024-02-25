package com.example.simpleimdbapp.data.repository.imdb

import com.example.simpleimdbapp.data.api.imdb.GetGenresApiResponse
import com.example.simpleimdbapp.data.api.imdb.ImdbApiService
import com.example.simpleimdbapp.data.repository.BaseRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImdbRepository @Inject constructor(private val apiService: ImdbApiService) :
    BaseRepository() {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getGenres(): Flow<ApiResponse<GetGenresApiResponse>> {
        return safeApiCall { apiService.getGenres() }
    }
}