package com.frogtest.movieguru.presentation.search

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.frogtest.movieguru.domain.model.movie.Movie
import com.frogtest.movieguru.presentation.movies.MovieGridItem
import com.frogtest.movieguru.presentation.movies.MovieListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


private const val TAG = "MovieScreen"

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    useGrid: Boolean = true,
) {
    val context = LocalContext.current

    val movies = viewModel.searchedMovies.collectAsLazyPagingItems()
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = movies.loadState.refresh is LoadState.Loading)

    val showFiltersDialog = remember { mutableStateOf(false) }
    if (showFiltersDialog.value) {
        SearchFilterDialog(
            onDismiss = { showFiltersDialog.value = false },
        )
    }

    LaunchedEffect(key1 = movies.loadState) {
        if (movies.loadState.refresh is LoadState.Error) {
            if (movies.itemCount > 0)
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
            AnimatedVisibility(visible = true) {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                SearchWidget(
                    text = viewModel.searchQuery.value,
                    onSearchEvent = viewModel::onSearchEvent,
                    navigateBack = { navController.popBackStack() },
                    useGridView = useGrid,
                    onFilterClicked = { showFiltersDialog.value = true },
                    modifier = Modifier
                        .padding(8.dp)
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
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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

                    if (useGrid)
                        GridContent(movies, navController)
                    else
                        ListContent(movies, navController)
                }
            }
        }
    }


}

@Composable
fun GridContent(movies: LazyPagingItems<Movie>, navController: NavController) {
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
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp)
                    )
                }

        }
    }
}

@Composable
fun ListContent(movies: LazyPagingItems<Movie>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = movies.itemCount,
            key = movies.itemKey { it.id }
        ) {
            val item = movies[it]

            item?.let { itm ->
                MovieListItem(
                    movie = itm,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            navController.navigate("movie/${itm.id}")
                        }
                )
            }

        }
        item {
            if (movies.loadState.append is LoadState.Loading)
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
        }
    }
}

