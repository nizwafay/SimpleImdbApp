package com.example.simpleimdbapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simpleimdbapp.ui.feature.genres.GenresScreen
import com.example.simpleimdbapp.ui.feature.moviedetail.MovieDetailScreen
import com.example.simpleimdbapp.ui.feature.movies.MoviesScreen

private const val GENRES_DESTINATION = "genres"
const val GENRE_ID_KEY = "genre_id"
const val GENRE_NAME_KEY = "genre_name"

private const val MOVIES_DESTINATION = "movies"
const val MOVIE_ID_KEY = "movie_id"
const val MOVIE_TITLE_KEY = "movie_title"

private const val MOVIE_DETAIL_DESTINATION = "movie_detail"

@Composable
fun AppNavigation() {
    // Create a NavHost with the start destination
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = GENRES_DESTINATION
    ) {
        // Define screens and their routes
        composable(GENRES_DESTINATION) {
            GenresScreen {
                navController.navigate("$MOVIES_DESTINATION?genreId=${it.id}&genreName=${it.name}")
            }
        }
        composable(
            route = "$MOVIES_DESTINATION?genreId={$GENRE_ID_KEY}&genreName={$GENRE_NAME_KEY}",
            arguments = listOf(
                navArgument(GENRE_ID_KEY) { type = NavType.IntType },
                navArgument(GENRE_NAME_KEY) { type = NavType.StringType },
            )
        ) {
            MoviesScreen(
                onNavigateToMovieDetailScreen = {
                    navController.navigate("$MOVIE_DETAIL_DESTINATION?movieId=${it.id}&movieTitle=${it.title}")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "$MOVIE_DETAIL_DESTINATION?movieId={$MOVIE_ID_KEY}&movieTitle={$MOVIE_TITLE_KEY}",
            arguments = listOf(
                navArgument(MOVIE_ID_KEY) { type = NavType.IntType },
                navArgument(MOVIE_TITLE_KEY) { type = NavType.StringType },
            )
        ) {
            MovieDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}