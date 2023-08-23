package com.frogtest.movieguru.components.movie_listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.frogtest.movieguru.domain.model.movie.Movie

@Composable
fun PagingDataErrorWidget(
    retry: () -> Unit,
    errorMessage: String?,
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage ?: "An unexpected error occurred",
            color = MaterialTheme.colorScheme.error
        )
        Button(onClick = retry) {
            Text(text = "Retry")
        }
    }
}