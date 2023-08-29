package com.c4entertainment.moviehunter.presentation.sign_in

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.c4entertainment.moviehunter.navigation.Screen
import com.c4entertainment.moviehunter.presentation.sign_in.components.SignInContents

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    onGoogleSignInClick: () -> Unit,
    checkBiometricsAndNavigate: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    navController: NavController,
) {

    val TAG = "SignInScreen"

    val context = LocalContext.current


    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
           SignInContents(
               padding = PaddingValues(16.dp),
               passwordSignIn = { email, password ->
                   viewModel.signInWithEmailAndPassword(email, password)
               },
               googleSignIn = onGoogleSignInClick,
               navigateToForgotPasswordScreen = {
                   Toast.makeText(context, "To be implemented", Toast.LENGTH_SHORT)
                       .show()
               },
               navigateToSignUpScreen = { navController.navigate(Screen.SignUpScreen.route) },
               errorMessage = state.message
           )
       }

    LaunchedEffect(key1 = state.message) {
        Log.d(TAG, "SignInScreen: ${state.message}")
        state.message?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (viewModel.getSignedInUser !== null) {
            if (viewModel.isEmailVerified) {
                checkBiometricsAndNavigate()
            } else {
                navController.navigate(Screen.VerifyEmailScreen.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            if (viewModel.isEmailVerified) {
                checkBiometricsAndNavigate()
                viewModel.resetState()
            } else {
                navController.navigate(Screen.VerifyEmailScreen.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }
    }

}