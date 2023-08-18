package com.frogtest.movieguru.presentation.sign_up.components

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.frogtest.movieguru.presentation.sign_up.SignUpViewModel
import com.frogtest.movieguru.util.Resource

@Composable
fun SignUp(
    viewModel: SignUpViewModel = hiltViewModel(),
    sendEmailVerification: () -> Unit,
    showVerifyEmailMessage: () -> Unit,
    navigateBack: () -> Unit
) {

    //tag
    val TAG = "SignUp"

    when(val signUpResponse = viewModel.signUpResponse) {
        is Resource.Loading -> CircularProgressIndicator()
        is Resource.Success -> {
            val isUserSignedUp = signUpResponse.data
            LaunchedEffect(isUserSignedUp) {
                if (isUserSignedUp == true) {
                    sendEmailVerification()
                    showVerifyEmailMessage()
                    navigateBack()
                    
                }
            }
        }
        is Resource.Error -> signUpResponse.apply {
            LaunchedEffect(errorMessage) {
                Log.d(TAG, "SignUp: $errorMessage")
            }
        }
    }
}