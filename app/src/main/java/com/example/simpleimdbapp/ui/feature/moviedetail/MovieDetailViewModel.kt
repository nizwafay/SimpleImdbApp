package com.example.simpleimdbapp.ui.feature.moviedetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleimdbapp.data.api.imdb.GetMovieDetailApiResponse
import com.example.simpleimdbapp.data.repository.imdb.ImdbRepository
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Review
import com.example.simpleimdbapp.ui.components.ListState
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

    // region Reviews
    val reviews = mutableStateListOf<Review>()
    val getReviewsErrorMessage = mutableStateOf("")

    private var getReviewsPage by mutableIntStateOf(1)
    var getReviewsCanPaginate by mutableStateOf(false)
    var getReviewsListState by mutableStateOf(ListState.IDLE)
    // endregion

    init {
        getMovieDetail()
        getMovieReviews()
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

    fun getMovieReviews() {
        viewModelScope.launch {
            if (getReviewsPage == 1 || (getReviewsPage != 1 && getReviewsCanPaginate) && getReviewsListState != ListState.LOADING) {
                getReviewsListState = ListState.LOADING

                imdbRepository.getMovieReviews(
                    movieId = movieId,
                    page = getReviewsPage,
                ).collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            getReviewsCanPaginate = it.data.page < it.data.totalPages

                            if (getReviewsPage == 1) {
                                reviews.clear()
                                reviews.addAll(it.data.results)
                            } else {
                                reviews.addAll(it.data.results)
                            }

                            getReviewsListState = ListState.IDLE

                            if (getReviewsCanPaginate)
                                getReviewsPage++
                        }

                        is ApiResponse.Error -> {
                            getReviewsListState = ListState.ERROR
                            getReviewsErrorMessage.value = it.errorMessage
                        }

                        is ApiResponse.Loading -> {
                            getReviewsListState = ListState.LOADING
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        getReviewsPage = 1
        getReviewsListState = ListState.IDLE
        getReviewsCanPaginate = false
        super.onCleared()
    }
}