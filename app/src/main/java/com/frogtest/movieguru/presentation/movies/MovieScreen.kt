package com.frogtest.movieguru.presentation.movies

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.frogtest.movieguru.components.MovieContent
import com.frogtest.movieguru.navigation.Screen
import com.frogtest.movieguru.util.MovieTVFilterConfig


private const val TAG = "MovieScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    useGrid: Boolean = true,
    movieTV: String = MovieTVFilterConfig.MOVIE,
    showSettingsDialog: () -> Unit,
) {

    val movies = viewModel.searchedMovies.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar =
        {
            MovieScreenTopBar(
                photoUrl = viewModel.getSignedInUser?.photoUrl,
                isGridView = useGrid,
                movieEvent = viewModel::onMovieEvent,
                onSettingsClicked = showSettingsDialog,
                onSearchClicked = {
                    viewModel.onMovieEvent(
                        MovieEvent.OnMovieTVBackupStored(movieTV)
                    )
                    navController.navigate(Screen.SearchScreen.route)
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        MovieContent(
            modifier = Modifier.padding(paddingValues),
            useGrid = useGrid,
            moviesOrTV = movieTV,
            content = movies,
            toggleMovie = {
                viewModel.onMovieEvent(
                    MovieEvent.OnMovieTVToggled(MovieTVFilterConfig.MOVIE)
                )
            },
            toggleTV = {
                viewModel.onMovieEvent(
                    MovieEvent.OnMovieTVToggled(MovieTVFilterConfig.TV)
                )
            },
            onClickMovie = { id ->
                navController.navigate("movie/${movieTV}/${id}")

            },
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

