package com.frogtest.movieguru.presentation.movies

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MovieScreenTopBar(
    photoUrl: String? = null,
    isGridView: Boolean,
    movieEvent: (MovieEvent) -> Unit,
    onSearchClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        title = { Text(text = "Movie Guru") },
        actions =
        {

            IconButton(
                onClick = {onSearchClicked()}
            )
            {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

//            IconButton(onClick = { onFilterClicked() })
//            {
//                Icon(
//                    imageVector = Icons.Default.FilterList,
//                    contentDescription = "Filter",
//                    tint = MaterialTheme.colorScheme.onSurface
//                )
//            }
            IconButton(onClick = {movieEvent(MovieEvent.OnToggleView(!isGridView))} ) {
                Icon(
                    imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                    contentDescription = "View",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(onClick = onSettingsClicked)
            {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}