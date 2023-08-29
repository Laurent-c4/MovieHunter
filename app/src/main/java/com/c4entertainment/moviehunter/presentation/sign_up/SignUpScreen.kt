package com.c4entertainment.moviehunter.presentation.sign_up

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.c4entertainment.moviehunter.presentation.sign_up.components.SendEmailVerification
import com.c4entertainment.moviehunter.presentation.sign_up.components.SignUp
import com.c4entertainment.moviehunter.presentation.sign_up.components.SignUpContent
import com.c4entertainment.moviehunter.util.Constants.VERIFY_EMAIL_MESSAGE

@Composable
@ExperimentalComposeUiApi
fun SignUpScreen(
    viewModel: SignUpViewModel,
    navigateBack: () -> Unit,
    navigateToAuthCheck: () -> Unit
) {
    val context = LocalContext.current

    SignUpContent(
        padding = PaddingValues(16.dp),
        signUp = { email, password ->
            viewModel.signUpWithEmailAndPassword(email, password)
        },
        navigateBack = navigateBack,
        errorMessage = viewModel.signUpResponse.errorMessage
    )

    SignUp(
        sendEmailVerification = {
            viewModel.sendEmailVerification()
        },
        showVerifyEmailMessage = {
            Toast.makeText(context, VERIFY_EMAIL_MESSAGE, Toast.LENGTH_LONG).show()
        },
        navigateBack = navigateToAuthCheck
    )

    SendEmailVerification()
}