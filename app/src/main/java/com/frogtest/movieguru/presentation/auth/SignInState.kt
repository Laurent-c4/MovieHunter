package com.frogtest.movieguru.presentation.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val error: String? = null
)
