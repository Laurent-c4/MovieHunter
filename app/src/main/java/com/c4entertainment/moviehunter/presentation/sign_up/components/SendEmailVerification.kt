package com.c4entertainment.moviehunter.presentation.sign_up.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.c4entertainment.moviehunter.presentation.sign_up.SignUpViewModel
import com.c4entertainment.moviehunter.util.Resource

@Composable
fun SendEmailVerification(
    viewModel: SignUpViewModel = hiltViewModel()
) {
    when(val sendEmailVerificationResponse = viewModel.sendEmailVerificationResponse) {
        is Resource.Loading -> {
            if (sendEmailVerificationResponse.isLoading) {
              CircularProgressIndicator()
            }
        }
        is Resource.Success -> Unit
        is Resource.Error -> sendEmailVerificationResponse.apply {
            LaunchedEffect(errorMessage) {
                print(errorMessage)
            }
        }
    }
}