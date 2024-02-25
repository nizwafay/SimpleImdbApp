package com.example.simpleimdbapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simpleimdbapp.ui.feature.genres.GenresScreen
import com.example.simpleimdbapp.ui.feature.movies.MoviesScreen

private const val GENRES_DESTINATION = "genres"
const val GENRE_ID_KEY = "genre_id"
const val GENRE_NAME_KEY = "genre_name"

private const val MOVIES_DESTINATION = "movies"

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
            MoviesScreen {
                navController.popBackStack()
            }
        }
    }
}