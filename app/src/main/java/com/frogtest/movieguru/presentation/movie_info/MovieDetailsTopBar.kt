package com.frogtest.movieguru.presentation.movie_info

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MovieDetailsTopBar(
    title: String = "Movie Guru",
    showVideos: Boolean = false,
    onShowVideosClicked: (show: Boolean) -> Unit,
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = navigateBack ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions =
        {

            IconButton(onClick = {onShowVideosClicked(!showVideos)})
            {

                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = "Show Videos",
                        tint = if(showVideos) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }

        }
    )
}