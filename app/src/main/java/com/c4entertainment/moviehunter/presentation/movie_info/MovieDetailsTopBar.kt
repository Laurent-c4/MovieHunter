package com.c4entertainment.moviehunter.presentation.movie_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.c4entertainment.moviehunter.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MovieDetailsTopBar(
    title: String = stringResource(id = R.string.app_name),
    navigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        title = { Text(
            text = title,
            maxLines = 2,
        ) },
        navigationIcon = {
            IconButton(onClick = navigateBack ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }, scrollBehavior = scrollBehavior
    )
}