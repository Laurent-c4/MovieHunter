package com.c4entertainment.moviehunter.presentation.movies

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.c4entertainment.moviehunter.MainActivityUiState
import com.c4entertainment.moviehunter.components.MovieContent
import com.c4entertainment.moviehunter.domain.model.movie.Movie
import com.c4entertainment.moviehunter.navigation.Screen
import com.c4entertainment.moviehunter.presentation.sign_in.UserProfile
import com.c4entertainment.moviehunter.util.MovieTVFilterConfig


private const val TAG = "MovieScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    showSettingsDialog: () -> Unit,
) {

    val settingsState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    val movies = viewModel.searchedMovies.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val useGrid = shouldUseGrid(settingsState)
    val movieTV = shouldShowMovieOrTV(settingsState)

    MovieScreen(
        movies = movies,
        scrollBehavior = scrollBehavior,
        movieEvent = viewModel::onMovieEvent,
        navigate = navController::navigate,
        showSettingsDialog = showSettingsDialog,
        useGrid = useGrid,
        movieTV = movieTV,
        userPhoto = viewModel.getSignedInUser?.photoUrl,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    movies: LazyPagingItems<Movie>,
    scrollBehavior: TopAppBarScrollBehavior,
    movieEvent: (MovieEvent) -> Unit,
    navigate: (String) -> Unit,
    showSettingsDialog: () -> Unit,
    useGrid: Boolean,
    movieTV: String,
    userPhoto: String?,

    ) {


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MovieScreenTopBar(photoUrl = userPhoto, isGridView = useGrid, toggleView = {
                movieEvent(MovieEvent.OnToggleView(!useGrid))
            }, onSettingsClicked = showSettingsDialog, onSearchClicked = {
                movieEvent(MovieEvent.OnMovieTVBackupStored(movieTV))
                navigate(Screen.SearchScreen.route)
            }, scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        MovieContent(
            modifier = Modifier.padding(paddingValues),
            useGrid = useGrid,
            moviesOrTV = movieTV,
            content = movies,
            toggleMovie = { movieEvent(MovieEvent.OnMovieTVToggled(MovieTVFilterConfig.MOVIE)) },
            toggleTV = { movieEvent(MovieEvent.OnMovieTVToggled(MovieTVFilterConfig.TV)) },
            onClickMovie = { id -> navigate("movie/${movieTV}/${id}") },
        )
    }

    LaunchedEffect(key1 = movies.loadState) {
        if (movies.loadState.refresh is LoadState.Error) {
            Log.e(
                TAG,
                "MovieScreen: Load Error" + (movies.loadState.refresh as LoadState.Error).error.message
            )
        }
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

