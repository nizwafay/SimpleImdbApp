package com.example.simpleimdbapp.ui.feature.genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.simpleimdbapp.data.api.imdb.GetGenresApiResponse
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Genre
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresScreen(
    modifier: Modifier = Modifier,
    viewModel: GenresViewModel = hiltViewModel(),
    onNavigateToMoviesScreen: (Genre) -> Unit,
) {
    val genres by viewModel.genres.collectAsState()

    Scaffold(
        topBar = { ImdbTopAppBar("Genres") },
        modifier = modifier.fillMaxWidth(),
    ) {
        Surface(modifier = Modifier.padding(it)) {
            when (genres) {
                is ApiResponse.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items((genres as ApiResponse.Success<GetGenresApiResponse>).data.genres) { genre ->
                            GenreButton(genre = genre.name) {
                                onNavigateToMoviesScreen(genre)
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
                        Text(text = (genres as ApiResponse.Error).errorMessage)
                        Button(onClick = {
                            viewModel.getGenres()
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
                        CircularProgressIndicator()
                    }
                }
            }
        }

    }
}

@Composable
fun GenreButton(
    genre: String,
    onClick: () -> Unit,
) {
    ElevatedButton(onClick = onClick) {
        Text(genre)
    }
}