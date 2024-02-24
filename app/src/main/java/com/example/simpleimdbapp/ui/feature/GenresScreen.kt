package com.example.simpleimdbapp.ui.feature

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar
import com.example.simpleimdbapp.ui.theme.SimpleImdbAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresScreen(
    modifier: Modifier = Modifier,
) {
    val genres =
        listOf("Adventure", "Action", "Hollywood", "Wow", "Adventure", "Action", "Hollywood", "Wow")

    Scaffold(
        topBar = { ImdbTopAppBar("Genres") },
        modifier = modifier.fillMaxWidth(),
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(it),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(genres) { item ->
                GenreButton(genre = item) {

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

@Preview(showBackground = true)
@Composable
fun GenresScreenPreview() {
    SimpleImdbAppTheme {
        GenresScreen()
    }
}