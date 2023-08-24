package com.frogtest.movieguru.presentation.movie_info

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.frogtest.movieguru.MainActivity
import com.frogtest.movieguru.util.Constants.VIDEO_ID
import com.frogtest.movieguru.util.Constants.VIDEO_START_DELAY
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YoutubePlayer(
    youtubeVideoID: String,
) {

    val context = LocalContext.current
    var videoStartDelay = 0F

    lateinit var ytPlayer: YouTubePlayer

    val mainActivity = context as MainActivity

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data

                videoStartDelay = intent?.getFloatExtra(VIDEO_START_DELAY, 0F) ?: 0F
                ytPlayer.seekTo(videoStartDelay)
                ytPlayer.play()
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {

        AndroidView(
            factory = {

                YouTubePlayerView(it).apply {
                    val youTubePlayerView = this

                    val iFramePlayerOptions = IFramePlayerOptions.Builder()
                        .controls(1)
                        .fullscreen(1) // enable full screen button
                        .build()

                    // we need to initialize manually in order to pass IFramePlayerOptions to the player
                    youTubePlayerView.enableAutomaticInitialization = false

                    youTubePlayerView.addFullscreenListener(object : FullscreenListener {
                        override fun onEnterFullscreen(
                            fullscreenView: View,
                            exitFullscreen: () -> Unit
                        ) {

                            //navigate to full screen activity
                            val intent = Intent(mainActivity, FullscreenActivity::class.java)
                            intent.putExtra(VIDEO_ID, youtubeVideoID)
                            intent.putExtra(VIDEO_START_DELAY, videoStartDelay)
                            exitFullscreen()

                            launcher.launch(intent)
                        }

                        override fun onExitFullscreen() {

                        }
                    })

                    youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            ytPlayer = youTubePlayer
                            youTubePlayer.cueVideo(youtubeVideoID, videoStartDelay)
                        }

                        override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                            videoStartDelay = second
                        }
                    }, iFramePlayerOptions)


                    findViewTreeLifecycleOwner()?.lifecycle?.addObserver(youTubePlayerView)
                }


            }
        )
    }
}