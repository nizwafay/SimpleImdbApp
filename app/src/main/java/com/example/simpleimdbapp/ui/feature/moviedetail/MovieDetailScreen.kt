package com.example.simpleimdbapp.ui.feature.moviedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simpleimdbapp.domain.model.ApiResponse
import com.example.simpleimdbapp.domain.model.imdb.Genre
import com.example.simpleimdbapp.domain.model.imdb.Review
import com.example.simpleimdbapp.ui.components.ErrorComponent
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar
import com.example.simpleimdbapp.ui.components.ListState
import com.example.simpleimdbapp.ui.components.LoadingComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val shouldStartPaginate = remember {
        derivedStateOf {
            viewModel.getReviewsCanPaginate && (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyListState.layoutInfo.totalItemsCount - 6)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && viewModel.getReviewsListState == ListState.IDLE)
            viewModel.getMovieReviews()
    }

    val movieResponse by viewModel.movie.collectAsState()

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
            when (movieResponse) {
                is ApiResponse.Success -> {
                    with((movieResponse as ApiResponse.Success).data) {
                        LazyColumn(
                            state = lazyListState,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                        ) {
                            item {
                                PrimaryInfo(
                                    modifier.padding(horizontal = 16.dp),
                                    title = title,
                                    yearRelease = releaseDate.take(4).toInt(),
                                    duration = runtime
                                )
                                Spacer(modifier.height(16.dp))
                                Synopsis(
                                    modifier.padding(horizontal = 16.dp),
                                    posterPath = posterPath,
                                    synopsis = overview
                                )
                                Spacer(modifier.height(16.dp))
                                Genres(
                                    genres = genres,
                                )
                            }

                            item {
                                ReviewsTitle()
                            }

                            if (viewModel.reviews.size > 0) {
                                items(viewModel.reviews) { review ->
                                    ReviewCard(
                                        modifier.padding(horizontal = 16.dp),
                                        review = review,
                                    )
                                }

                                item(key = viewModel.getReviewsListState) {
                                    when (viewModel.getReviewsListState) {
                                        ListState.ERROR -> {
                                            ErrorComponent(
                                                errorMessage = viewModel.getReviewsErrorMessage.value,
                                                onRetry = {
                                                    viewModel.getMovieReviews()
                                                })
                                        }

                                        ListState.LOADING -> {
                                            LoadingComponent()
                                        }

                                        else -> {}
                                    }
                                }
                            } else {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(text = "No review yet.")
                                    }
                                }
                            }
                        }
                    }
                }

                is ApiResponse.Error -> {
                    ErrorComponent(errorMessage = (movieResponse as ApiResponse.Error).errorMessage) {
                        viewModel.getMovieDetail()
                    }
                }

                is ApiResponse.Loading -> {
                    LoadingComponent()
                }

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
        modifier = modifier
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
        modifier.fillMaxWidth()
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
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(genres) {
            SuggestionChip(
                onClick = { },
                label = { Text(text = it.name) },
            )
        }
    }
}

@Composable
fun ReviewsTitle(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            contentColor = Color.White,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Reviews",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    review: Review,
) {
    ElevatedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.author,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = review.updatedAt.take(10),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.content,
                style = MaterialTheme.typography.bodySmall,
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

@Preview
@Composable
fun ReviewCardPreview() {
    Surface {
        ReviewCard(
            review = Review(
                author = "Dave09",
                content = "One of the best animated films I have ever seen. Great characters, amusing animation, and laugh-out-loud humor. Also, watch for the little skit shown after the credits. It's all great stuff that simply must be seen.",
                updatedAt = "2021-06-23T15:57:23.763Z",
            )
        )
    }
}