package com.c4entertainment.moviehunter.presentation.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchScreenTopBar(
    isGridView: Boolean,
    onViewToggled: (Boolean) -> Unit,
    onSearchClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Search") },
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

            

        }
    )
}