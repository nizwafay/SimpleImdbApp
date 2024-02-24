package com.example.simpleimdbapp.data.repository

import com.example.simpleimdbapp.domain.model.ApiResponse
import retrofit2.Response

abstract class BaseRepository {
    protected suspend fun <T> apiCall(
        apiCall: suspend () -> Response<T>,
    ): ApiResponse<T?> {
        return try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                ApiResponse.Success(response.body())
            } else {
                ApiResponse.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            ApiResponse.Error("Error: ${e.message}")
        }
    }
}
