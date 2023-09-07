package com.c4entertainment.moviehunter.presentation.movie_info

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    id: String,
    type: String,
    viewModel: MovieDetailsViewModel,
    navigateBack: () -> Unit
) {

    val TAG = "MovieDetailsScreen"

    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    Log.d(TAG, "MovieDetailsScreen: ${state.movieDetails})")


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MovieDetailsTopBar(
                title = state.movie?.title ?: "",
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->


        val tabIndex = viewModel.tabIndex
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = tabIndex.value!!) {
                viewModel.tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex.value!! == index,
                        onClick = { viewModel.updateTabIndex(index) },
                    )
                }
            }

            val ytVideos =
                state.movieDetails?.videos?.results?.filter { videos -> videos.site == "YouTube" }?.reversed()

            when (tabIndex.value) {
                0 -> MovieDetails(
                    state = state,
                    swipeRefreshState = swipeRefreshState,
                    viewModel = viewModel,
                    id = id,
                    type = type,
                    modifier = Modifier
                        .fillMaxSize()
                        .draggable(
                            state = viewModel.dragState.value!!,
                            orientation = Orientation.Horizontal,
                            onDragStarted = { },
                            onDragStopped = {
                                viewModel.updateTabIndexBasedOnSwipe()
                            }),
                )

                1 -> MovieVideos(
                    modifier = Modifier
                        .fillMaxSize()
                        .draggable(
                            state = viewModel.dragState.value!!,
                            orientation = Orientation.Horizontal,
                            onDragStarted = { },
                            onDragStopped = {
                                viewModel.updateTabIndexBasedOnSwipe()
                            }),
                    movieVideos = ytVideos,
                    navigateBack = navigateBack
                    )
            }
        }
    }
}



