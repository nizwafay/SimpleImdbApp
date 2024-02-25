package com.example.simpleimdbapp.data.repository.imdb

import com.example.simpleimdbapp.data.api.imdb.ImdbApiService
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Genre
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import retrofit2.Response

class ImdbRepositoryTest {
    private lateinit var apiService: ImdbApiService
    private lateinit var imdbRepository: ImdbRepository

    @Before
    fun setup() {
        apiService = mock()
        imdbRepository = ImdbRepository(apiService)
    }

    @Test
    fun fetchDataSuccess() = runTest {
        // Given
        val testData = listOf(
            Genre(
                id = 1, "Action",
            )
        )

        // When
        whenever(apiService.getGenres())
            .doReturn(Response.success(testData))

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
        whenever(apiService.getGenres())
            .doReturn(
                Response.error(
                    404,
                    ResponseBody.create(MediaType.get("application/json"), "")
                )
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
        whenever(apiService.getGenres())
            .thenThrow(RuntimeException("Test exception"))

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