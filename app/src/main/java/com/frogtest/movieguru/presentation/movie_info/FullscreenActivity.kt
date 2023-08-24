package com.frogtest.movieguru.presentation.movie_info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.frogtest.movieguru.R
import com.frogtest.movieguru.util.Constants.VIDEO_ID
import com.frogtest.movieguru.util.Constants.VIDEO_START_DELAY
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class FullscreenActivity : AppCompatActivity() {

    private lateinit var youTubePlayer: YouTubePlayer
    private lateinit var videoID: String
    private var  startVideoDelay: Float = 0F


    private var isFullscreen = false
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            sendBackPosition(startVideoDelay)
        }
    }

    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var fullscreenViewContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yt_fullscreen)


        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        fullscreenViewContainer = findViewById<FrameLayout>(R.id.full_screen_view_container)

        startVideoDelay = intent.getFloatExtra(VIDEO_START_DELAY, 0F)
        videoID = intent.getStringExtra(VIDEO_ID) ?: ""

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()

        // we need to initialize manually in order to pass IFramePlayerOptions to the player
        youTubePlayerView.enableAutomaticInitialization = false

        youTubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                // the video will continue playing in fullscreenView
                youTubePlayerView.visibility = View.GONE
                fullscreenViewContainer.visibility = View.VISIBLE
                fullscreenViewContainer.addView(fullscreenView)
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                // the video will continue playing in the player
                youTubePlayerView.visibility = View.VISIBLE
                fullscreenViewContainer.visibility = View.GONE
                fullscreenViewContainer.removeAllViews()

                sendBackPosition(startVideoDelay)
            }
        })

        youTubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@FullscreenActivity.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo(videoID, startVideoDelay)
                youTubePlayer.toggleFullscreen()
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                startVideoDelay = second
            }
        }, iFramePlayerOptions)

        lifecycle.addObserver(youTubePlayerView)

    }

    private fun sendBackPosition(videoStartDelay: Float) {
        val intent = Intent()
        intent.putExtra(VIDEO_START_DELAY, videoStartDelay)
        setResult(RESULT_OK, intent)
        finish()
    }

    // Hide the status bar and navigation bar
    override fun onResume() {
        super.onResume()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youTubePlayerView.release()
    }

}
