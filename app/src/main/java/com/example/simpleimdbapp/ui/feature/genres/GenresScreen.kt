package com.example.simpleimdbapp.ui.feature.genres

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpleimdbapp.data.api.imdb.GetGenresApiResponse
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresScreen(modifier: Modifier = Modifier, viewModel: GenresViewModel = viewModel()) {
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

                            }
                        }
                    }
                }

                is ApiResponse.Error -> {
                    Text(text = (genres as ApiResponse.Error).errorMessage)
                }

                is ApiResponse.Loading -> {
                    Text(text = "Loading")
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