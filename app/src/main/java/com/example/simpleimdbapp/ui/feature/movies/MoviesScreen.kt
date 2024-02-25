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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simpleimdbapp.data.api.imdb.GetMoviesApiResponse
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val movies by viewModel.movies.collectAsState()

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
            when (movies) {
                is ApiResponse.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items((movies as ApiResponse.Success<GetMoviesApiResponse>).data.results) { movie ->
                            MovieCard(
                                title = movie.title,
                                posterPath = movie.posterPath,
                            ) {

                            }
                        }
                    }
                }

                is ApiResponse.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = (movies as ApiResponse.Error).errorMessage)
                        Button(onClick = {
                            viewModel.getMovies()
                        }) {
                            Text(text = "Retry")
                        }
                    }
                }

                is ApiResponse.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Loading...")
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