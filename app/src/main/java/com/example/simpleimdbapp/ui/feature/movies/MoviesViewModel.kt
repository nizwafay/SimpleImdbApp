package com.example.simpleimdbapp.ui.feature.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.MovieSnippet
import com.example.simpleimdbapp.ui.components.ListState
import com.example.simpleimdbapp.ui.navigation.GENRE_ID_KEY
import com.example.simpleimdbapp.ui.navigation.GENRE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val imdbRepository: ImdbRepository,
) : ViewModel() {
    private val genreId = checkNotNull(savedStateHandle.get<Int>(GENRE_ID_KEY))
    val genreName = checkNotNull(savedStateHandle.get<String>(GENRE_NAME_KEY))

    val movies = mutableStateListOf<MovieSnippet>()
    val errorMessage = mutableStateOf("")

    private var page by mutableIntStateOf(1)
    var canPaginate by mutableStateOf(false)
    var listState by mutableStateOf(ListState.IDLE)

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            if (page == 1 || (page != 1 && canPaginate) && listState != ListState.LOADING) {
                listState = ListState.LOADING

                imdbRepository.getMovies(
                    page = page,
                    withGenres = genreId.toString()
                ).collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            canPaginate = it.data.page < it.data.totalPages

                            if (page == 1) {
                                movies.clear()
                                movies.addAll(it.data.results)
                            } else {
                                movies.addAll(it.data.results)
                            }

                            listState = ListState.IDLE

                            if (canPaginate)
                                page++
                        }

                        is ApiResponse.Error -> {
                            listState = ListState.ERROR
                            errorMessage.value = it.errorMessage
                        }

                        is ApiResponse.Loading -> {
                            listState = ListState.LOADING
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        page = 1
        listState = ListState.IDLE
        canPaginate = false
        super.onCleared()
    }
}