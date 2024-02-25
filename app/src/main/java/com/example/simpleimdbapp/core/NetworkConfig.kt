package com.example.simpleimdbapp.core

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/3/"

private const val token =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNTNiNzBkYmM5N2E2NTUzNzFlOTg4M2YyODUxNjA2NCIsInN1YiI6IjY1ZDUyMWU1NWNhNzA0MDE3YzBjODlhNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BnonsyU-FFPM6TAQec7qYy4UjQoVaWS74YjC5RRjgN0"

fun createRetrofitClient(): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
    .build()

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .header("accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}

val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor(token))
    .build()

fun createMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()