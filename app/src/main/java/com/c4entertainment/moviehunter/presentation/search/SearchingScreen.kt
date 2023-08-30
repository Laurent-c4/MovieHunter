package com.c4entertainment.moviehunter.presentation.search

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.c4entertainment.moviehunter.components.MovieContent
import com.c4entertainment.moviehunter.util.MovieTVFilterConfig


private const val TAG = "MovieScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel,
) {

    val movies = viewModel.searchedMovies.collectAsLazyPagingItems()
    val settingsState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    val useGrid = shouldUseGrid(settingsState)
    val movieTV = shouldShowMovieOrTV(settingsState)

    val showFiltersDialog = remember { mutableStateOf(false) }
    if (showFiltersDialog.value) {
        SearchFilterDialog(
            onDismiss = { showFiltersDialog.value = false },
        )
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
                    navigateBack = {
                        viewModel.onSearchEvent(SearchEvent.ClearSearch)
                        viewModel.onSearchEvent(SearchEvent.OnMovieTVToggled(viewModel.getBackupMovieTV()))
                        navController.popBackStack()
                                   },
                    isGridView = useGrid,
                    onFilterClicked = { showFiltersDialog.value = true },
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
    ) { paddingValues ->
        MovieContent(
            modifier = Modifier.padding(paddingValues),
            useGrid = useGrid,
            moviesOrTV = movieTV,
            content = movies,
            toggleMovie = {
                viewModel.onSearchEvent(
                    SearchEvent.OnMovieTVToggled(MovieTVFilterConfig.MOVIE)
                )
            },
            toggleTV = {
                viewModel.onSearchEvent(
                    SearchEvent.OnMovieTVToggled(MovieTVFilterConfig.TV)
                )
            },
            onClickMovie = {id ->navController.navigate("movie/${movieTV}/${id}")},
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

    BackHandler {
        viewModel.onSearchEvent(SearchEvent.ClearSearch)
        viewModel.onSearchEvent(SearchEvent.OnMovieTVToggled(viewModel.getBackupMovieTV()))
        navController.popBackStack()
    }
}

@Composable
private fun shouldUseGrid(
    uiState: SettingsUiState,
): Boolean = when (uiState) {
    SettingsUiState.Loading -> false
    is SettingsUiState.Success -> uiState.settings.useGrid
}

@Composable
private fun shouldShowMovieOrTV(
    uiState: SettingsUiState,
): String = when (uiState) {
    SettingsUiState.Loading -> MovieTVFilterConfig.MOVIE
    is SettingsUiState.Success -> uiState.settings.movieTV
}


