package com.frogtest.movieguru.presentation.movie_info

import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.frogtest.movieguru.R
import com.frogtest.movieguru.domain.model.movie_details.MovieVideo
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

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
//                        icon = {
//                            when (index) {
//                                0 -> Icon(
//                                    imageVector = Icons.Default.Details,
//                                    contentDescription = null
//                                )
//
//                                1 -> Icon(
//                                    imageVector = Icons.Default.VideoLibrary,
//                                    contentDescription = null
//                                )
//                            }
//                        }
                    )
                }
            }

            val ytVideos =
                state.movieDetails?.videos?.results?.filter { videos -> videos.site == "YouTube" }?.sortedBy { it.publishedAt }

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


                    )
            }
        }
    }


}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun MovieDetails(
    state: MovieDetailsState,
    swipeRefreshState: SwipeRefreshState,
    viewModel: MovieDetailsViewModel,
    id: String,
    type: String,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        if (state.error == null) {
            Column(modifier) {

                SwipeRefresh(
                    modifier = modifier,
                    state = swipeRefreshState,
                    onRefresh = { viewModel.getMovie(id.toInt(), type) },
                    indicator = { refreshState, refreshTrigger ->
                        SwipeRefreshIndicator(
                            state = refreshState,
                            refreshTriggerDistance = refreshTrigger,
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        state.movie?.let { movie ->
                            item {
                                MovieDetailsPoster(movie = movie)
                            }

                            item {
                                Divider(Modifier.padding(top = 8.dp, bottom = 16.dp))
                            }

                            item {
                                Text(
                                    text = movie.overview,
                                    Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                                )
                            }

                            val rating: Int = (movie.voteAverage * 10).roundToInt()
                            if (rating > 0)
                                item {
                                    Text(
                                        text = "Rating: $rating %",
                                        Modifier.padding(
                                            start = 8.dp,
                                            end = 8.dp,
                                            bottom = 8.dp
                                        )
                                    )
                                }


                            var date = movie.releaseDate
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                                val d = LocalDate.parse(date)
                                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
                                date = d.format(formatter)
                            }

                            item {
                                Text(
                                    text = "Release Date: $date",
                                    Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                                )
                            }

                            state.movieDetails?.genres?.let { genres ->
                                item {
                                    Text(
                                        text = "Genres: ${genres.joinToString { it.name ?: "" }}",
                                        Modifier.padding(
                                            start = 8.dp,
                                            end = 8.dp,
                                            bottom = 8.dp
                                        )
                                    )
                                }
                            }
                        }

                        state.movieDetails?.credits?.cast?.let { cast ->
                            val actors = cast.map { actor -> actor.name }
                            item {
                                Text(
                                    text = "Cast: ${actors.take(5).joinToString()}",
                                    Modifier.padding(start = 8.dp, end = 8.dp)
                                )
                            }
                        }

                        item {
                            Divider(Modifier.padding(top = 16.dp, bottom = 8.dp))
                        }
                    }

                }

            }

        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error
                    )
                    // TODO: Add retry button
//                Button(onClick = { // TODO:
//                     }) {
//                    Text(text = "Retry")
//                }
                }
            }
        }
    }
}

@Composable
private fun MovieVideos(
    modifier: Modifier = Modifier,
    movieVideos: List<MovieVideo>?,
) {

    val showVideo = remember { mutableStateOf(false) }
    val vidId = remember { mutableStateOf("") }

    val w = (480f / LocalDensity.current.density * 2.7f) - 80
    val h = (360 / LocalDensity.current.density * 2.7f) - 138

    Box(modifier) {
        movieVideos?.let { videos ->
            LazyColumn(
                modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(
                    count = videos.size,
                    key = { videos[it].key ?: "" },
                    itemContent = { index ->

                        Column {
                            Box(
                                modifier = Modifier
                                    .requiredWidth(w.dp)
                                    .requiredHeight(h.dp)
                                    .clip(shape = RoundedCornerShape(30.dp))
                                    .padding(8.dp)
                                    .clickable {
                                        vidId.value = videos[index].key ?: ""
                                        showVideo.value = true
                                    }
                            ) {
                                AsyncImage(
                                    model = "https://img.youtube.com/vi/${videos[index].key ?: ""}/0.jpg",
                                    placeholder = painterResource(id = R.drawable.baseline_movie_24),
                                    error = painterResource(R.drawable.baseline_movie_24),
                                    contentDescription = "Youtube Thumbnail",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .fillMaxHeight(h)
                                        .clipToBounds()
                                )
                            }

                            Text(
                                text = videos[index].name ?: "",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Divider(Modifier.padding(bottom = 8.dp))
                        }


                    }
                )
            }
        }

        if (showVideo.value) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .align(Alignment.Center)
                    .clickable {
                        showVideo.value = false
                    },
            ) {
                YoutubePlayer(
                    youtubeVideoID = vidId.value,
                )
            }


        }
    }
}



