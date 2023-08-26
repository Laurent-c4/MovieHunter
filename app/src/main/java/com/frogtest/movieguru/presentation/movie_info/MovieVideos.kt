package com.frogtest.movieguru.presentation.movie_info

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.frogtest.movieguru.R
import com.frogtest.movieguru.domain.model.movie_details.MovieVideo

@Composable
fun MovieVideos(
    modifier: Modifier = Modifier,
    movieVideos: List<MovieVideo>?,
    navigateBack: () -> Unit
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
//                    contentType = { videos[it].key ?: "" },
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

    BackHandler {
        if (showVideo.value) {
            showVideo.value = false
        } else navigateBack()
    }
}