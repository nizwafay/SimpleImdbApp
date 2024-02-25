package com.example.simpleimdbapp.core

import com.example.simpleimdbapp.data.api.imdb.ImdbApiService
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideImdbApiService(): ImdbApiService {
        return createRetrofitClient().create(ImdbApiService::class.java)
    }

    @Provides
    fun provideImdbRepository(imdbApiService: ImdbApiService): ImdbRepository {
        return ImdbRepository(imdbApiService)
    }
}