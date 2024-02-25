package com.example.simpleimdbapp.ui.feature.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import com.example.simpleimdbapp.ui.navigation.MOVIE_ID_KEY
import com.example.simpleimdbapp.ui.navigation.MOVIE_TITLE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val imdbRepository: ImdbRepository,
) : ViewModel() {
    private val movieId = checkNotNull(savedStateHandle.get<Int>(MOVIE_ID_KEY))
    val movieTitle = checkNotNull(savedStateHandle.get<String>(MOVIE_TITLE_KEY))
}