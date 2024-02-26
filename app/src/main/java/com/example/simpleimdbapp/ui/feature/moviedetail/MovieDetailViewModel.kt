package com.example.simpleimdbapp.ui.feature.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleimdbapp.data.api.imdb.GetMovieDetailApiResponse
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.ui.navigation.MOVIE_ID_KEY
import com.example.simpleimdbapp.ui.navigation.MOVIE_TITLE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val imdbRepository: ImdbRepository,
) : ViewModel() {
    private val movieId = checkNotNull(savedStateHandle.get<Int>(MOVIE_ID_KEY))
    val movieTitle = checkNotNull(savedStateHandle.get<String>(MOVIE_TITLE_KEY))

    private val _movie =
        MutableStateFlow<ApiResponse<GetMovieDetailApiResponse>>(ApiResponse.Loading)
    val movie: StateFlow<ApiResponse<GetMovieDetailApiResponse>> get() = _movie

    init {
        getMovieDetail()
    }

    fun getMovieDetail() {
        viewModelScope.launch {
            imdbRepository.getMovieDetail(movieId)
                .onEach {
                    _movie.value = it
                }
                .launchIn(this)
        }
    }
}