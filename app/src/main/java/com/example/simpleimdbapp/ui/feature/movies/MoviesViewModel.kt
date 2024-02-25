package com.example.simpleimdbapp.ui.feature.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleimdbapp.data.api.imdb.GetMoviesApiResponse
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.ui.navigation.GENRE_ID_KEY
import com.example.simpleimdbapp.ui.navigation.GENRE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val imdbRepository: ImdbRepository,
) : ViewModel() {
    private val genreId = checkNotNull(savedStateHandle.get<Int>(GENRE_ID_KEY))
    val genreName = checkNotNull(savedStateHandle.get<String>(GENRE_NAME_KEY))

    private val _movies = MutableStateFlow<ApiResponse<GetMoviesApiResponse>>(ApiResponse.Loading)
    val movies: StateFlow<ApiResponse<GetMoviesApiResponse>> get() = _movies

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            imdbRepository.getMovies(
                withGenres = genreId.toString()
            ).onEach {
                _movies.value = it
            }.launchIn(this)
        }
    }
}