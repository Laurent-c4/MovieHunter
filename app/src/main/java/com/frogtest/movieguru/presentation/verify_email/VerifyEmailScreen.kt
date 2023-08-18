package com.frogtest.movieguru.presentation.verify_email

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.frogtest.movieguru.navigation.Screen
import com.frogtest.movieguru.presentation.sign_in.SignInViewModel
import com.frogtest.movieguru.presentation.verify_email.components.VerifyEmailContent
import com.frogtest.movieguru.util.Constants.EMAIL_NOT_VERIFIED_MESSAGE
import com.frogtest.movieguru.util.Constants.VERIFY_EMAIL_MESSAGE

@Composable
fun VerifyEmailScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    VerifyEmailContent(
        padding = PaddingValues(16.dp),
        resendVerificationEmail = {
            viewModel.reSendVerificationEmail()
        },
        reloadUser = {
           viewModel.reloadUser()
        }
    )

    LaunchedEffect(key1 = state.isEmailVerified) {
        if (state.isEmailVerified) {
            navigateToHomeScreen()
        }
    }

    LaunchedEffect(key1 = state.isEmailVerificationSent) {
        if (state.isEmailVerificationSent) {
            Toast.makeText(context, VERIFY_EMAIL_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(key1 = state.message) {
        state.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

}