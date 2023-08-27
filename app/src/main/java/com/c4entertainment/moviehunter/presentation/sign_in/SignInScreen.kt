package com.c4entertainment.moviehunter.presentation.sign_in

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.c4entertainment.moviehunter.presentation.sign_in.components.SignInContents

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    state: SignInState,
    onGoogleSignInClick: () -> Unit,
    onPasswordSignInClick: (email: String, password: String) -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {

    val TAG = "SignInScreen"

    val context = LocalContext.current
    LaunchedEffect(key1 = state.message) {
        Log.d(TAG, "SignInScreen: ${state.message}")
        state.message?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
           SignInContents(
               padding = PaddingValues(16.dp),
               passwordSignIn = onPasswordSignInClick,
               googleSignIn = onGoogleSignInClick,
               navigateToForgotPasswordScreen = navigateToForgotPasswordScreen,
               navigateToSignUpScreen = navigateToSignUpScreen,
               errorMessage = state.message
           )
       }


    

}