package com.frogtest.movieguru.presentation.sign_up.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.frogtest.movieguru.presentation.sign_up.SignUpViewModel
import com.frogtest.movieguru.util.Resource

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