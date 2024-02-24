package com.example.simpleimdbapp.data.repository.imdb

import com.example.simpleimdbapp.data.api.imdb.ImdbApiService
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Genre
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    fun fetchDataSuccess() = runBlocking {
        // Given
        val testData = listOf(
            Genre(
                id = 1, "Action",
            )
        )
        whenever(apiService.getGenres())
            .doReturn(Response.success(testData))

        // When
        val result = imdbRepository.getGenres()

        // Then
        assertTrue(result is ApiResponse.Success)
        assertEquals(testData, (result as ApiResponse.Success).data)
    }

    @Test
    fun fetchDataFailure() = runBlocking {
        // Given
        whenever(apiService.getGenres())
            .doReturn(
                Response.error(
                    404,
                    ResponseBody.create(MediaType.get("application/json"), "")
                )
            )

        // When
        val result = imdbRepository.getGenres()

        // Then
        assertTrue(result is ApiResponse.Error)
        assertEquals("Error: 404", (result as ApiResponse.Error).errorMessage)
    }

    @Test
    fun fetchDataException() = runBlocking {
        // Given
        whenever(apiService.getGenres())
            .thenThrow(RuntimeException("Test exception"))

        // When
        val result = imdbRepository.getGenres()

        // Then
        assertTrue(result is ApiResponse.Error)
        assertEquals("Error: Test exception", (result as ApiResponse.Error).errorMessage)
    }
}