package com.c4entertainment.moviehunter.presentation.sign_in

data class SignInResult(
    val data: UserProfile?,
    val error: String?
)

data class UserProfile(
    val id: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?
)
