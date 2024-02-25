package com.example.simpleimdbapp.ui.feature.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleimdbapp.data.api.imdb.GetGenresApiResponse
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(private val imdbRepository: ImdbRepository) :
    ViewModel() {
    private val _genres =
        MutableStateFlow<ApiResponse<GetGenresApiResponse>>(ApiResponse.Loading)
    val genres: StateFlow<ApiResponse<GetGenresApiResponse>> get() = _genres

    init {
        getGenres()
    }

    fun getGenres() {
        viewModelScope.launch {
            imdbRepository.getGenres()
                .onEach {
                    _genres.value = it
                }
                .launchIn(this)
        }
    }
}