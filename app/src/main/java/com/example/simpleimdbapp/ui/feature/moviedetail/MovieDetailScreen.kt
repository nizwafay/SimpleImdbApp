package com.example.simpleimdbapp.ui.feature.moviedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simpleimdbapp.domain.model.imdb.Genre
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            ImdbTopAppBar(
                viewModel.movieTitle,
                hasBackButton = true,
                onBackButtonClicked = onBack,
            )
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column {
                PrimaryInfo(
                    title = "Finding Nemo",
                    yearRelease = "2003-05-30".take(4).toInt(),
                    duration = 100
                )
                Synopsis(
                    posterPath = "/eHuGQ10FUzK1mdOY69wF5pGgEf5.jpg",
                    synopsis = "Nemo, an adventurous young clownfish, is unexpectedly taken from his Great Barrier Reef home to a dentist's office aquarium. It's up to his worrisome father Marlin and a friendly but forgetful fish Dory to bring Nemo home -- meeting vegetarian sharks, surfer dude turtles, hypnotic jellyfish, hungry seagulls, and more along the way."
                )
                Genres(
                    genres = listOf(
                        Genre(16, "Animation"),
                        Genre(10751, "Family"),
                        Genre(16, "Animation"),
                        Genre(10751, "Family"),
                        Genre(16, "Animation"),
                        Genre(10751, "Family"),
                        Genre(16, "Animation"),
                        Genre(10751, "Family"),
                    )
                )
            }
        }
    }
}

@Composable
fun PrimaryInfo(
    modifier: Modifier = Modifier,
    title: String,
    yearRelease: Int,
    duration: Int,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                text = yearRelease.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$duration min",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
fun Synopsis(
    modifier: Modifier = Modifier,
    posterPath: String,
    synopsis: String,
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/w780$posterPath").crossfade(true).build(),
            contentDescription = "Poster photo",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .aspectRatio(2f / 3f)
                .weight(2f),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = synopsis,
            modifier = Modifier.weight(3f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Genres(
    modifier: Modifier = Modifier,
    genres: List<Genre>
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(genres) {
            SuggestionChip(
                onClick = { },
                label = { Text(text = it.name) },
            )
        }
    }
}

@Preview
@Composable
fun PrimaryInfoPrev() {
    Surface {
        PrimaryInfo(
            title = "Finding Nemo",
            yearRelease = "2003-05-30".take(4).toInt(),
            duration = 100
        )
    }
}

@Preview
@Composable
fun SynopsisPreview() {
    Surface {
        Synopsis(
            posterPath = "/eHuGQ10FUzK1mdOY69wF5pGgEf5.jpg",
            synopsis = "Nemo, an adventurous young clownfish, is unexpectedly taken from his Great Barrier Reef home to a dentist's office aquarium. It's up to his worrisome father Marlin and a friendly but forgetful fish Dory to bring Nemo home -- meeting vegetarian sharks, surfer dude turtles, hypnotic jellyfish, hungry seagulls, and more along the way."
        )
    }
}

@Preview
@Composable
fun GenresPreview() {
    Surface {
        Genres(
            genres = listOf(
                Genre(16, "Animation"),
                Genre(10751, "Family"),
                Genre(16, "Animation"),
                Genre(10751, "Family"),
                Genre(16, "Animation"),
                Genre(10751, "Family"),
                Genre(16, "Animation"),
                Genre(10751, "Family"),
            )
        )
    }
}