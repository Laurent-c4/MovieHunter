package com.c4entertainment.moviehunter.presentation.sign_in

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c4entertainment.moviehunter.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :ViewModel() {

    companion object {
        private const val TAG = "SignInViewModel"
    }

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    val getSignedInUser get() = authRepository.getSignedInUser()

    fun onSignInResult(result: SignInResult) {
        _state.update {it.copy(
            isSignInSuccessful = result.data != null,
            message = result.error
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }


    suspend fun googleSignInIntentSender() = authRepository.googleSignInIntentSender()

    suspend fun googleSignInWithIntent(intent: Intent) = authRepository.googleSignInWithIntent(intent)


    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
        val response = authRepository.firebaseSignInWithEmailAndPassword(email, password)

        Log.d(TAG, "signInWithEmailAndPassword: " + response.errorMessage)

        _state.update {
            it.copy(
                isSignInSuccessful = response.data != null,
                message = response.errorMessage
            )
        }

    }
    }

    fun reloadUser() = viewModelScope.launch {
        val response = authRepository.reloadFirebaseUser()

        Log.d(TAG, "reloadUser: " + response.data)
        Log.d(TAG, "userVerified: $isEmailVerified")

        _state.update { it.copy(
            isEmailVerified = isEmailVerified,
            message = if (!isEmailVerified) "Please verify your email" else response.errorMessage,
            isEmailVerificationSent = false
            ) }
    }
    val isEmailVerified get() = authRepository.currentUser?.isEmailVerified ?: false

    fun reSendVerificationEmail() = viewModelScope.launch {
        _state.update { it.copy(isEmailVerificationSent = false) }

        val response = authRepository.sendEmailVerification()
        _state.update { it.copy(isEmailVerificationSent = response.data == true,
            message = response.errorMessage)}
    }
}