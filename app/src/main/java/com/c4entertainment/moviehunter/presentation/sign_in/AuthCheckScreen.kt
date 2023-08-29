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

@Composable
fun AuthCheckScreen(
    checkBiometricsAndNavigate: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    navController: NavController,
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    )

    LaunchedEffect(key1 = Unit) {
        if (viewModel.getSignedInUser !== null) {
            if (viewModel.isEmailVerified) {
                checkBiometricsAndNavigate()
            } else {
                navController.navigate(Screen.VerifyEmailScreen.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        } else {
            navController.navigate(Screen.SignInScreen.route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

}