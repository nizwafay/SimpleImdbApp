package com.example.simpleimdbapp.ui.feature.movies

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.simpleimdbapp.ui.components.ImdbTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    genreId: Int?,
    genreName: String?,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            ImdbTopAppBar(
                "$genreName Movies",
                hasBackButton = true,
                onBackButtonClicked = onBack,
            )
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        Surface(modifier = Modifier.padding(it)) {

        }
    }
}