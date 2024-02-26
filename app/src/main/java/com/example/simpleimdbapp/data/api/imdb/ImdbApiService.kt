package com.example.simpleimdbapp.data.api.imdb

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImdbApiService {
    @GET("genre/movie/list")
    suspend fun getGenres(@Query("language") language: String = "en"): Response<GetGenresApiResponse>

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("language") language: String = "en",
        @Query("include_video") includeVideo: Boolean = true,
        @Query("with_genres") withGenres: String? = null,
        @Query("page") page: Int = 1,
    ): Response<GetMoviesApiResponse>

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") id: Int
    ): Response<GetMovieDetailApiResponse>
}