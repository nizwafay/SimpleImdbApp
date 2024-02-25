package com.example.simpleimdbapp.ui.feature.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simpleimdbapp.domain.model.imdb.MovieSnippet
import com.example.simpleimdbapp.ui.components.ErrorComponent
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar
import com.example.simpleimdbapp.ui.components.ListState
import com.example.simpleimdbapp.ui.components.LoadingComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = hiltViewModel(),
    onNavigateToMovieDetailScreen: (MovieSnippet) -> Unit,
    onBack: () -> Unit,
) {
    val lazyGridListState = rememberLazyGridState()

    val shouldStartPaginate = remember {
        derivedStateOf {
            viewModel.canPaginate && (lazyGridListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyGridListState.layoutInfo.totalItemsCount - 6)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && viewModel.listState == ListState.IDLE)
            viewModel.getMovies()
    }

    Scaffold(
        topBar = {
            ImdbTopAppBar(
                "${viewModel.genreName} Movies",
                hasBackButton = true,
                onBackButtonClicked = onBack,
            )
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyVerticalGrid(
                    state = lazyGridListState,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(viewModel.movies) { movie ->
                        MovieCard(
                            title = movie.title,
                            posterPath = movie.posterPath,
                            onClick = {
                                onNavigateToMovieDetailScreen(movie)
                            },
                        )
                    }

                    item(
                        key = viewModel.listState,
                        span = {
                            GridItemSpan(maxLineSpan)
                        },
                    ) {
                        when (viewModel.listState) {
                            ListState.ERROR -> {
                                ErrorComponent(errorMessage = viewModel.errorMessage.value) {
                                    viewModel.getMovies()
                                }
                            }

                            ListState.LOADING -> {
                                LoadingComponent()
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    title: String,
    posterPath: String,
    onClick: () -> Unit,
) {
    ElevatedCard(onClick = onClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w780$posterPath").crossfade(true).build(),
                contentDescription = "Poster photo",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.aspectRatio(2f / 3f),
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .height(48.dp)
            ) {
                Text(
                    title,
                    modifier = Modifier.align(Alignment.Center),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}