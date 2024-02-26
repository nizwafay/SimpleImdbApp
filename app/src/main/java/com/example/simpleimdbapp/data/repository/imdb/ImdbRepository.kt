package com.example.simpleimdbapp.data.repository.imdb

import com.example.simpleimdbapp.data.api.imdb.GetGenresApiResponse
import com.example.simpleimdbapp.data.api.imdb.GetMovieDetailApiResponse
import com.example.simpleimdbapp.data.api.imdb.GetMoviesApiResponse
import com.example.simpleimdbapp.data.api.imdb.ImdbApiService
import com.example.simpleimdbapp.data.repository.BaseRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImdbRepository @Inject constructor(private val apiService: ImdbApiService) :
    BaseRepository() {

    suspend fun getGenres(): Flow<ApiResponse<GetGenresApiResponse>> {
        return safeApiCall { apiService.getGenres() }
    }

    suspend fun getMovies(
        page: Int = 1, withGenres: String? = null
    ): Flow<ApiResponse<GetMoviesApiResponse>> {
        return safeApiCall { apiService.getMovies(page = page, withGenres = withGenres) }
    }

    suspend fun getMovieDetail(id: Int): Flow<ApiResponse<GetMovieDetailApiResponse>> {
        return safeApiCall { apiService.getMovieDetail(id) }
    }
}