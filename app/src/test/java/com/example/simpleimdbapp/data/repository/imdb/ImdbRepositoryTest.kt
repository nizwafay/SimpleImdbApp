package com.example.simpleimdbapp.data.repository.imdb

import com.example.simpleimdbapp.data.api.imdb.GetGenresApiResponse
import com.example.simpleimdbapp.data.api.imdb.ImdbApiService
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Genre
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ImdbRepositoryTest {
    private lateinit var apiService: ImdbApiService
    private lateinit var imdbRepository: ImdbRepository

    @Before
    fun setup() {
        apiService = mockk<ImdbApiService>()
        imdbRepository = ImdbRepository(apiService)
    }

    @Test
    fun fetchDataSuccess() = runTest {
        // Given
        val testData = GetGenresApiResponse(
            genres = listOf(
                Genre(
                    id = 1, "Action",
                )
            )
        )

        // When
        coEvery { apiService.getGenres() } returns Response.success(testData)

        // Then
        imdbRepository.getGenres().collect { result ->
            when (result) {
                is ApiResponse.Loading -> {
                    println("Loading state emitted")
                }

                is ApiResponse.Success -> {
                    assertEquals(testData, result.data)
                }

                is ApiResponse.Error -> {
                    fail()
                }
            }
        }
    }

    @Test
    fun fetchDataFailure() = runTest {
        // When
        coEvery { (apiService.getGenres()) } returns Response.error(
            404,
            ResponseBody.create("application/json".toMediaType(), "")
        )

        // Then
        imdbRepository.getGenres().collect { result ->
            when (result) {
                is ApiResponse.Loading -> {
                    println("Loading state emitted")
                }

                is ApiResponse.Success -> {
                    fail()
                }

                is ApiResponse.Error -> {
                    assertEquals("Error: 404", result.errorMessage)
                }
            }
        }
    }

    @Test
    fun fetchDataException() = runTest {
        // When
        coEvery { apiService.getGenres() } throws RuntimeException("Test exception")

        // Then
        imdbRepository.getGenres().collect { result ->
            when (result) {
                is ApiResponse.Loading -> {
                    println("Loading state emitted")
                }

                is ApiResponse.Success -> {
                    fail()
                }

                is ApiResponse.Error -> {
                    assertEquals("Error: Test exception", result.errorMessage)
                }
            }
        }
    }
}