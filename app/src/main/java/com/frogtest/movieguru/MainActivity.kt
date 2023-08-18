package com.frogtest.movieguru

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.frogtest.movieguru.navigation.Screen
import com.frogtest.movieguru.presentation.sign_in.GoogleAuthUIClient
import com.frogtest.movieguru.presentation.sign_in.SignInScreen
import com.frogtest.movieguru.presentation.sign_in.SignInViewModel
import com.frogtest.movieguru.presentation.movie_info.MovieDetailsScreen
import com.frogtest.movieguru.presentation.movie_info.MovieDetailsViewModel
import com.frogtest.movieguru.presentation.movies.MovieScreen
import com.frogtest.movieguru.presentation.movies.MovieViewModel
import com.frogtest.movieguru.presentation.profile.SettingsDialog
import com.frogtest.movieguru.ui.theme.MovieGuruTheme
import com.google.android.gms.auth.api.identity.Identity
import com.frogtest.movieguru.preferences.SettingsDataStore
import com.frogtest.movieguru.presentation.sign_up.SignUpScreen
import com.frogtest.movieguru.presentation.sign_up.SignUpViewModel
import com.frogtest.movieguru.presentation.verify_email.VerifyEmailScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var settingsDataStore: SettingsDataStore
    private val viewModel by viewModels<MainViewModel>()

    private val googleAuthUIClient by lazy {
        GoogleAuthUIClient(context = applicationContext, oneTapClient = Identity.getSignInClient(applicationContext))
    }

    private val TAG = "MainActivity"

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private var keepSplash = true
    private val delay = 1000L
    private fun setupSplashScreen(splashScreen: SplashScreen) {
        // Replace this timer with your logic to load data on the splash screen.
        splashScreen.setKeepOnScreenCondition { keepSplash }
        Handler(Looper.getMainLooper()).postDelayed({
            keepSplash = false
        }, delay)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        setupSplashScreen(splashScreen = splashScreen)

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

                    val biometricsInitialised = remember { mutableStateOf(false) }
                    val useGrid = settingsDataStore.useGrid.value
                    if (!biometricsInitialised.value) {
                        initBiometrics(navController)
                        biometricsInitialised.value = true
                    }

                    val showSettingsDialog = remember { mutableStateOf(false) }
                    if (showSettingsDialog.value) {
                        SettingsDialog(
                            userData = googleAuthUIClient.getSignedInUser(),
                            onDismiss = { showSettingsDialog.value = false },
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthUIClient.signOut()
                                    navController.navigate(Screen.SignInScreen.route) {
                                        popUpTo(navController.graph.id) { inclusive = true }
                                    }
                                }

                                showSettingsDialog.value = false
                            },
                            onToggleUseGrid = {
                                settingsDataStore.toggleUseGrid()
                            },
                            useGrid = useGrid
                        )
                  }

                  Scaffold(
                      modifier = Modifier.fillMaxSize(),
                      topBar =
                      {
                          if (currentDestination != Screen.SignInScreen.route) {
                              TopBar(currentDestination = currentDestination,
                                  showSettingsDialog = showSettingsDialog,
                                  onSearchClicked = {
//                                      navController.navigate("search")
                                  })
                          }
                      },
                  ) { paddingValues ->
                      NavSetUp(navController, paddingValues)

                  }

            }
        }
    } }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun TopBar(
        currentDestination: String?,
        showSettingsDialog: MutableState<Boolean>,
        onSearchClicked: () -> Unit = {}
    ) {
        TopAppBar(
            title = { Text(text = "Movie Guru") },
            actions =
            {

                    // TODO:  Implement search
//                    IconButton(onClick = { onSearchClicked() })
//                    {
//                        Icon(
//                            imageVector = Icons.Default.Search,
//                            contentDescription = "Search",
//                            tint = MaterialTheme.colorScheme.onSurface
//                        )
//                    }

                    IconButton(onClick = { showSettingsDialog.value = true })
                    {
                        if(googleAuthUIClient.getSignedInUser()?.photoUrl != null) {
                        AsyncImage(
                            model = googleAuthUIClient.getSignedInUser()?.photoUrl,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .clip(shape = CircleShape),
                            contentScale = ContentScale.Crop,)
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
            }
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun NavSetUp(
        navController: NavHostController,
        paddingValues: PaddingValues,
        useGrid: Boolean = settingsDataStore.useGrid.value
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.SignInScreen.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {

            composable(Screen.SignInScreen.route) {
                val viewModel = hiltViewModel<SignInViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUIClient.getSignedInUser() !== null) {
                        if (viewModel.isEmailVerified) {
                            checkBiometricsAndNavigate(navController = navController)
                        } else {
                            navController.navigate(Screen.VerifyEmailScreen.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
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
                        if (viewModel.isEmailVerified) {
                            checkBiometricsAndNavigate(navController =  navController)
                            viewModel.resetState()
                        } else {
                            navController.navigate(Screen.VerifyEmailScreen.route) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                }

                SignInScreen(
                    state = state,
                    onGoogleSignInClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUIClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                    onPasswordSignInClick = { email, password ->
                        viewModel.signInWithEmailAndPassword(email, password)
                    },
                    navigateToForgotPasswordScreen = {},
                    navigateToSignUpScreen = {navController.navigate(Screen.SignUpScreen.route) }
                )
            }
            composable(Screen.VerifyEmailScreen.route){
                VerifyEmailScreen(navigateToHomeScreen = { navController.navigate(Screen.MovieScreen.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                } })
            }
            composable(Screen.SignUpScreen.route){
                val viewModel = hiltViewModel<SignUpViewModel>()

                SignUpScreen(viewModel = viewModel, navigateBack = {navController.popBackStack()})
            }
            composable(Screen.MovieScreen.route) {
                val viewModel = hiltViewModel<MovieViewModel>()
                MovieScreen(navController = navController, viewModel = viewModel, useGrid = useGrid)
            }
            composable("movie/{imdbID}") { backStackEntry ->
                val imdbID = backStackEntry.arguments?.getString("imdbID")
                val viewModel = hiltViewModel<MovieDetailsViewModel>()
                MovieDetailsScreen(imdbID = imdbID ?: "", viewModel = viewModel)
            }
        }
    }


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

                    if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                        navController.navigate(Screen.MovieScreen.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    navController.navigate(Screen.MovieScreen.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometrics")
            .setSubtitle("Confirm fingerprint to continue")
            .setNegativeButtonText("Use account password")
            .build()
    }

    private fun checkBiometricsAndNavigate(
        useFingerprint: Boolean = true,
        navController: NavHostController
    ) {
        if (useFingerprint) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            navController.navigate(Screen.MovieScreen.route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }
}