package com.example.simpleimdbapp.domain.model

sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val errorMessage: String) : ApiResponse<T>()
    object Loading : ApiResponse<Nothing>()
}
