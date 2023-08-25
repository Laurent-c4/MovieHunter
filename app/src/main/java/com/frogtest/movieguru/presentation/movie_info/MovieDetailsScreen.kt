package com.frogtest.movieguru.presentation.movie_info

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.frogtest.movieguru.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    id: String,
    type: String,
    viewModel: MovieDetailsViewModel,
    showVideos: Boolean,
    navigateBack: () -> Unit
) {

    val TAG = "MovieDetailsScreen"

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)
    val showVideo = remember { mutableStateOf(false) }
    val vidId = remember { mutableStateOf("") }

    Log.d(TAG, "MovieDetailsScreen: ${state.movieDetails})")


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MovieDetailsTopBar(
                title = state.movie?.title ?: "",
                showVideos = showVideos,
                onShowVideosClicked = { viewModel.toggleShowVideos(!showVideos) },
                navigateBack = navigateBack
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (state.error == null) {
                Column(Modifier.fillMaxSize()) {

                    SwipeRefresh(
                        modifier = Modifier.weight(1f),
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

                    val ytVideos =
                        state.movieDetails?.videos?.results?.filter { videos -> videos.site == "YouTube" }


                    AnimatedVisibility(
                        visible = (ytVideos?.isNotEmpty() == true && showVideos),

                        ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {

                            ytVideos?.let { videos ->
                                Box {

                                    HorizontalPager(
                                        pageCount = ytVideos.size,
                                        state = pagerState,
                                        key = { ytVideos[it].key ?: "" },
                                    ) { index ->
//                                        YoutubePlayer(
//                                            youtubeVideoID = videos[index].key ?: "",
////                                            launcher  = launcher
//                                        )
                                        val w = (480f / LocalDensity.current.density * 2.7f) - 100
                                        val h = (360 / LocalDensity.current.density * 2.7f) - 138

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
//                                                .fillMaxWidth(w)
                                                    .fillMaxHeight(h)
                                                    .clip(shape = RoundedCornerShape(30.dp))
                                            )
                                        }
                                    }

                                }

                                if (ytVideos.size > 1) {
                                    Box(
                                        modifier = Modifier
                                            .offset(y = -(16).dp)
                                            .fillMaxWidth(0.5f)
                                            .clip(RoundedCornerShape(100))
                                            .background(MaterialTheme.colorScheme.background)
//                    .padding(8.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                    {
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                                }
                                            },
                                            modifier = Modifier.align(Alignment.CenterStart)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowLeft,
                                                contentDescription = "Previous"
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                                }
                                            },
                                            modifier = Modifier.align(Alignment.CenterEnd)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowRight,
                                                contentDescription = "Next"
                                            )
                                        }
                                    }
                                }
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

            if (showVideo.value) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
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


}

@Composable
fun videoDialog(context: Context, videoID: String) {


}