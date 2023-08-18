package com.frogtest.movieguru.presentation.sign_in

data class SignInResult(
    val data: UserData?,
    val error: String?
)

data class UserData(
    val id: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?
)
