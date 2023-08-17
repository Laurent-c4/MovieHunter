package com.frogtest.movieguru.presentation.movies

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.frogtest.movieguru.domain.model.Movie


private const val TAG = "MovieScreen"

@Composable
fun MovieScreen(
    movies: LazyPagingItems<Movie>,
    navController: NavController
) {
    val context = LocalContext.current
    
    LaunchedEffect(key1 = movies.loadState) {
        if(movies.loadState.refresh is LoadState.Error) {
            Toast.makeText(context,
                "Error: " + (movies.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG).show()
            Log.e(TAG, "MovieScreen: Load Error" + (movies.loadState.refresh as LoadState.Error).error.message)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if(movies.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(movies) {
                    if (it != null)
                        MovieItem(
                            movie = it,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    navController.navigate("movie/${it.imdbID}")
                                }
                        )
                }
                item {
                  if (movies.loadState.append is LoadState.Loading)
                      CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

}