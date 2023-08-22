package com.frogtest.movieguru.presentation.movies

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.frogtest.movieguru.presentation.profile.SettingsDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


private const val TAG = "MovieScreen"

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    useGrid: Boolean = true,
    showSettingsDialog: () -> Unit,
) {
    val context = LocalContext.current

    val movies = viewModel.searchedMovies.collectAsLazyPagingItems()
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = movies.loadState.refresh is LoadState.Loading)

    val showFiltersDialog = remember { mutableStateOf(false) }
    if (showFiltersDialog.value) {
        MovieFilterDialog(
            onDismiss = { showFiltersDialog.value = false },
        )
    }


    val showSearchBar = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = movies.loadState) {
        if (movies.loadState.refresh is LoadState.Error) {
            if (movies.itemCount > 0)
//                Toast.makeText(
//                    context,
//                    "Error: " + (movies.loadState.refresh as LoadState.Error).error.message,
//                    Toast.LENGTH_LONG
//                ).show()
                Log.e(
                    TAG,
                    "MovieScreen: Load Error" + (movies.loadState.refresh as LoadState.Error).error.message
                )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar =
        {
            MovieScreenTopBar(
                photoUrl = viewModel.getSignedInUser?.photoUrl,
                onSettingsClicked = showSettingsDialog,
                onFilterClicked = { showFiltersDialog.value = true },
                onSearchClicked = { showSearchBar.value = true }
            )

            AnimatedVisibility(visible = showSearchBar.value) {
                SearchWidget(
                    text = viewModel.searchQuery.value,
                    onTextChange = viewModel::onMovieEvent,
                    onSearchClicked = viewModel::onMovieEvent,
                    onDismiss = { showSearchBar.value = false },
                    onFilterClicked = { showFiltersDialog.value = true },
                )
            }

        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (movies.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (movies.itemCount == 0) {
                if (movies.loadState.refresh is LoadState.Error) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (movies.loadState.refresh as LoadState.Error).error.message
                                ?: "An unexpected error occurred",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { movies.refresh() }) {
                            Text(text = "Retry")
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Results",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            } else {

                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { movies.refresh() },
                    indicator = { state, refreshTrigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = refreshTrigger,
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                ) {

//                Column {
//                    OutlinedTextField(
//                        value = viewModel.searchQuery.value,
//                        onValueChange = {
//                            viewModel.onMovieEvent(
//                                MovieEvent.OnSearchQueryChange(it)
//                            )
//                        },
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .fillMaxWidth(),
//                        placeholder = {
//                            Text(text = "Search...")
//                        },
//                        maxLines = 1,
//                        singleLine = true
//                    )
//                }
                    if (useGrid)
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 128.dp),
                        ) {
                            items(
                                count = movies.itemCount,
                                key = movies.itemKey { it.id },
                                contentType = movies.itemContentType { it }
                            ) { index ->

                                val item = movies[index]

                                item?.let {
                                    MovieGridItem(
                                        movie = it,
                                        modifier = Modifier
//                                            .height(270.dp)
                                            .fillMaxHeight()
                                            .padding(start = 1.dp, end = 1.dp, bottom = 5.dp)
                                            .clickable {
                                                navController.navigate("movie/${it.id}")
                                            }
                                    )
                                }
                            }
                            item {
                                if (movies.loadState.append is LoadState.Loading)
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    else
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(
                                count = movies.itemCount,
                                key = movies.itemKey { it.id }
                            ) {
                                val item = movies[it]

                                if (item != null)
                                    MovieItem(
                                        movie = item,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                navController.navigate("movie/${item.id}")
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
    }


}