package com.example.simpleimdbapp.data.repository

import com.example.simpleimdbapp.domain.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

abstract class BaseRepository {
    protected suspend fun <T : Any> safeApiCall(
        apiCall: suspend () -> Response<T>,
    ): Flow<ApiResponse<T>> = flow {
        emit(ApiResponse.Loading)

        try {
            val response = apiCall.invoke()
            if (response.isSuccessful) {
                emit(ApiResponse.Success(response.body()!!))
            } else {
                emit(ApiResponse.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error("Error: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}
