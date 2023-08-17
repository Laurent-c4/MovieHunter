package com.frogtest.movieguru

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.frogtest.movieguru.presentation.auth.GoogleAuthUIClient
import com.frogtest.movieguru.presentation.auth.SignInScreen
import com.frogtest.movieguru.presentation.auth.SignInViewModel
import com.frogtest.movieguru.presentation.movie_info.MovieDetailsScreen
import com.frogtest.movieguru.presentation.movie_info.MovieDetailsViewModel
import com.frogtest.movieguru.presentation.movies.MovieScreen
import com.frogtest.movieguru.presentation.movies.MovieViewModel
import com.frogtest.movieguru.presentation.profile.ProfileScreen
import com.frogtest.movieguru.presentation.profile.SettingsDialog
import com.frogtest.movieguru.ui.theme.MovieGuruTheme
import com.google.android.gms.auth.api.identity.Identity
import com.sabasi.mobile.datastore.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val googleAuthUIClient by lazy {
        GoogleAuthUIClient(context = applicationContext, oneTapClient = Identity.getSignInClient(applicationContext))
}
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieGuruTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination?.route
                    Log.d(TAG, "onCreate: currentDestination: $currentDestination")

                    //set remember for showing and hiding settings dialog
                    val showSettingsDialog = remember { mutableStateOf(false) }

                    val biometricsInitialised = remember { mutableStateOf(false) }
                    val useFingerprint = settingsDataStore.useFingerprint.value

                    if (!biometricsInitialised.value) {
                        initBiometrics(navController)
                        biometricsInitialised.value = true
                    }

                    if (showSettingsDialog.value) {
                        SettingsDialog(
                            userData = googleAuthUIClient.getSignedInUser(),
                            onDismiss = { showSettingsDialog.value = false },
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthUIClient.signOut()
                                    navController.navigate("auth") {
                                        popUpTo("movies") { inclusive = true }
                                    }
                                }

                                showSettingsDialog.value = false
                            },
                            onToggleFingerprint = {
                                settingsDataStore.toggleUseFingerprint()
                            },
                            useFingerprint = useFingerprint
                        )
                  }

                  Scaffold(
                      modifier = Modifier.fillMaxSize(),
                      topBar =
                      {
                          if (currentDestination != "auth") {
                              TopAppBar(
                                  title = { Text(text = "Movie Guru") },
                                  actions =
                                  {
                                      if (currentDestination == "movies")
                                          IconButton(
                                              onClick = {
                                                    showSettingsDialog.value = true
                                              }
                                          )
                                          {
//                                              Icon(
//                                                  imageVector = Icons.Filled.Person,
//                                                  contentDescription = "Profile"
//                                              )

                                              AsyncImage(
                                                  model = googleAuthUIClient.getSignedInUser()?.photoUrl,
                                                  contentDescription = "Pic",
                                                  modifier = Modifier
                                                      .clip(shape = CircleShape),
                                                  contentScale = ContentScale.Crop,
                                              )


                                          }
                                  }
                              )
                          } else {
                          }
                      },
                  ) { paddingValues ->

                      NavHost(
                          navController = navController,
                          startDestination = "auth",
                          modifier = Modifier
                              .fillMaxSize()
                              .padding(paddingValues = paddingValues),
                      ) {

                          composable("auth") {
                              val viewModel = hiltViewModel<SignInViewModel>()
                              val state by viewModel.state.collectAsStateWithLifecycle()

                              LaunchedEffect(key1 = Unit) {
                                  if (googleAuthUIClient.getSignedInUser() !== null) {
                                      onNavigate(useFingerprint, navController)
                                  }
                              }

                              val launcher = rememberLauncherForActivityResult(
                                  contract = ActivityResultContracts.StartIntentSenderForResult(),
                                  onResult = {result ->
                                      if (result.resultCode == RESULT_OK) {
                                          lifecycleScope.launch {
                                              val signInResult = googleAuthUIClient.signInWithIntent(
                                                  intent = result.data ?: return@launch
                                              )
                                              viewModel.onSignInResult(signInResult)
                                          }
                                      }

                                  }
                              )

                              LaunchedEffect(key1 = state.isSignInSuccessful) {
                                  if (state.isSignInSuccessful) {
                                      onNavigate(useFingerprint, navController)
                                      viewModel.resetState()
                                  }
                              }

                              SignInScreen(
                                  state = state,
                                  onSignInClick = {
                                      lifecycleScope.launch {
                                          val signInIntentSender = googleAuthUIClient.signIn()
                                          launcher.launch(
                                              IntentSenderRequest.Builder(
                                                  signInIntentSender?:return@launch)
                                                  .build()
                                          )
                                      }
                                  }
                              )
                          }
                          composable("movies") {
                              val viewModel = hiltViewModel<MovieViewModel>()
                              val movies = viewModel.moviePagingFlow.collectAsLazyPagingItems()
                              MovieScreen(movies = movies, navController = navController )
                          }
                          composable("movie/{imdbID}") { backStackEntry ->
                              val imdbID = backStackEntry.arguments?.getString("imdbID")
                              val viewModel = hiltViewModel<MovieDetailsViewModel>()
//                              val movie = viewModel.getMovie(movieId = movieId ?: "")
                              MovieDetailsScreen(imdbID = imdbID ?: "", viewModel = viewModel )
                          }
                      }
                  }

            }
        }
    } }


    @Composable
    private fun initBiometrics(navController: NavHostController) {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                    navigate(navController)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Additional security")
            .setSubtitle("Confirm fingerprint to continue")
            .setNegativeButtonText("Use account password")
            .build()
    }

    private fun onNavigate(
        useFingerprint: Boolean,
        navController: NavHostController
    ) {
        if (true) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            navigate(navController)
        }
    }

    private fun navigate(navController: NavHostController) {
        navController.navigate("movies") {
            popUpTo("auth") { inclusive = true }
        }
    }
}