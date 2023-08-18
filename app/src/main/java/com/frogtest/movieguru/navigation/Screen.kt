package com.frogtest.movieguru.navigation

import com.frogtest.movieguru.util.Constants.FORGOT_PASSWORD_SCREEN
import com.frogtest.movieguru.util.Constants.MOVIE_DETAILS_SCREEN
import com.frogtest.movieguru.util.Constants.MOVIE_SCREEN
import com.frogtest.movieguru.util.Constants.PROFILE_SCREEN
import com.frogtest.movieguru.util.Constants.SIGN_IN_SCREEN
import com.frogtest.movieguru.util.Constants.SIGN_UP_SCREEN
import com.frogtest.movieguru.util.Constants.VERIFY_EMAIL_SCREEN

sealed class Screen(val route: String) {
    object SignInScreen: Screen(SIGN_IN_SCREEN)
    object ForgotPasswordScreen: Screen(FORGOT_PASSWORD_SCREEN)
    object SignUpScreen: Screen(SIGN_UP_SCREEN)

    object MovieScreen: Screen(MOVIE_SCREEN)

    object MovieDetailsScreen: Screen(MOVIE_DETAILS_SCREEN)

    object VerifyEmailScreen: Screen(VERIFY_EMAIL_SCREEN)

    object ProfileScreen: Screen(PROFILE_SCREEN)
}
