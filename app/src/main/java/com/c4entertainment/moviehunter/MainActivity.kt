package com.c4entertainment.moviehunter

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.c4entertainment.moviehunter.navigation.Screen
import com.c4entertainment.moviehunter.presentation.movie_info.MovieDetailsScreen
import com.c4entertainment.moviehunter.presentation.movie_info.MovieDetailsViewModel
import com.c4entertainment.moviehunter.presentation.movies.MovieScreen
import com.c4entertainment.moviehunter.presentation.movies.MovieViewModel
import com.c4entertainment.moviehunter.presentation.profile.SettingsDialog
import com.c4entertainment.moviehunter.presentation.search.SearchScreen
import com.c4entertainment.moviehunter.presentation.search.SearchViewModel
import com.c4entertainment.moviehunter.presentation.sign_in.AuthCheckScreen
import com.c4entertainment.moviehunter.presentation.sign_in.SignInScreen
import com.c4entertainment.moviehunter.presentation.sign_in.SignInViewModel
import com.c4entertainment.moviehunter.presentation.sign_up.SignUpScreen
import com.c4entertainment.moviehunter.presentation.sign_up.SignUpViewModel
import com.c4entertainment.moviehunter.presentation.verify_email.VerifyEmailScreen
import com.c4entertainment.moviehunter.ui.theme.MovieGuruTheme
import com.c4entertainment.moviehunter.util.DarkThemeConfig
import com.c4entertainment.moviehunter.util.ConnectivityObserver
import com.c4entertainment.moviehunter.util.MovieTVFilterConfig
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()

    private val TAG = "MainActivity"

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)
            val systemUiController = rememberSystemUiController()
            val useDynamicColor = useDynamicColor(uiState = uiState)
            val snackbarHostState = remember { SnackbarHostState() }
            val showSettingsDialog = remember { mutableStateOf(false) }
            val useFingerprint = shouldUseFingerPrint(uiState = uiState)


            val status by connectivityObserver.observe().collectAsState(
                initial = ConnectivityObserver.Status.Unavailable
            )
            val isOffline = status != ConnectivityObserver.Status.Available


            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }

            MovieGuruTheme(
                darkTheme = darkTheme,
                dynamicColor = useDynamicColor
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination?.route

                    val biometricsInitialised = remember { mutableStateOf(false) }
                    if (!biometricsInitialised.value) {
                        initBiometrics(navController)
                        biometricsInitialised.value = true
                    }

                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                    ) {

                        NavSetUp(
                            navController = navController,
                            useFingerPrint = useFingerprint,
                            showSettingsDialog = { showSettingsDialog.value = true },
                            paddingValues = it)

                    }

                    AuthState(navController = navController)

                    if (showSettingsDialog.value) {
                        SettingsDialog(
                            onDismiss = { showSettingsDialog.value = false },
                        )
                    }

                    // If user is not connected to the internet show a snack bar to inform them.
                    val notConnectedMessage = stringResource(R.string.not_connected)
                    Log.d(TAG, "onCreate isOffline: $isOffline")
                    LaunchedEffect(isOffline) {
                        if (isOffline) {
                            delay(1000)
                            snackbarHostState.showSnackbar(
                                message = notConnectedMessage,
                                duration = SnackbarDuration.Indefinite,
                            )
                        }
                    }

                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun NavSetUp(
        navController: NavHostController,
        useFingerPrint: Boolean,
        showSettingsDialog: () -> Unit = {},
        paddingValues: PaddingValues,
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.AuthCheckScreen.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            composable(Screen.AuthCheckScreen.route) {
                AuthCheckScreen(
                    checkBiometricsAndNavigate = {
                        checkBiometricsAndNavigate(
                            useFingerprint = useFingerPrint,
                            navController = navController
                        )
                    },
                    navController = navController,
                )
            }

            composable(Screen.SignInScreen.route) {

                val viewModel = hiltViewModel<SignInViewModel>()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = viewModel.googleSignInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                SignInScreen(
                    onGoogleSignInClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = viewModel.googleSignInIntentSender()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                    navController = navController,
                    viewModel = viewModel,
                    checkBiometricsAndNavigate = {
                        checkBiometricsAndNavigate(
                            useFingerprint = useFingerPrint,
                            navController = navController
                        )
                    }

                )
            }
            composable(Screen.VerifyEmailScreen.route) {
                VerifyEmailScreen(
                    navigateToHomeScreen = {
                        navController.navigate(Screen.MovieScreen.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                    showSettingsDialog = showSettingsDialog,
                    navigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.SignUpScreen.route) {
                val viewModel = hiltViewModel<SignUpViewModel>()

                SignUpScreen(
                    viewModel = viewModel,
                    navigateBack = { navController.popBackStack() },
                    navigateToAuthCheck = {
                        navController.navigate(Screen.AuthCheckScreen.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                )
            }
            composable(Screen.MovieScreen.route) {
                val viewModel = hiltViewModel<MovieViewModel>()
                MovieScreen(
                    navController = navController,
                    viewModel = viewModel,
                    showSettingsDialog = showSettingsDialog
                )
            }
            composable("movie/{type}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val type = backStackEntry.arguments?.getString("type")
                Log.d(TAG, "NavSetUp: Movie ID -  $id")
                val viewModel = hiltViewModel<MovieDetailsViewModel>()
                MovieDetailsScreen(
                    id = id ?: "",
                    type = type ?: "",
                    viewModel = viewModel,
                    navigateBack = { navController.popBackStack() },
                )
            }
            composable(Screen.SearchScreen.route) {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(
                    viewModel = viewModel,
                    navController = navController,
                )
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

    /**
     * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
     */
    @Composable
    private fun useDynamicColor(
        uiState: MainActivityUiState,
    ): Boolean = when (uiState) {
        MainActivityUiState.Loading -> false
        is MainActivityUiState.Success -> uiState.userSettings.useDynamicColor
    }

    /**
     * Returns `true` if dark theme should be used, as a function of the [uiState] and the
     * current system context.
     */
    @Composable
    private fun shouldUseDarkTheme(
        uiState: MainActivityUiState,
    ): Boolean = when (uiState) {
        MainActivityUiState.Loading -> isSystemInDarkTheme()
        is MainActivityUiState.Success -> when (uiState.userSettings.darkThemeConfig) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
            else -> {
                // Should never happen, but if it does, default to following the system
                // dark theme setting.
                isSystemInDarkTheme()
            }
        }
    }

    @Composable
    private fun shouldUseFingerPrint(
        uiState: MainActivityUiState,
    ): Boolean = when (uiState) {
        MainActivityUiState.Loading -> false
        is MainActivityUiState.Success -> uiState.userSettings.useFingerPrint
    }

    @Composable
    private fun AuthState(
        navController: NavHostController,
    ) {
        val isUserSignedOut = viewModel.getAuthState().collectAsState().value
        val currentDestination = navController.currentBackStackEntry?.destination?.route
        val authRoutes = listOf(
            Screen.SignInScreen.route,
            Screen.SignUpScreen.route,
            Screen.ForgotPasswordScreen.route,
            Screen.AuthCheckScreen.route,
        )

        currentDestination?.let { destination ->
            if (isUserSignedOut && destination !in authRoutes) {

                Log.d(TAG, "AuthState: $currentDestination")
                navController.navigate(Screen.SignInScreen.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }
}