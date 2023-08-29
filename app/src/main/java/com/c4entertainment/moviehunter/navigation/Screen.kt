package com.c4entertainment.moviehunter.navigation

import com.c4entertainment.moviehunter.util.Constants.AUTH_CHECK_SCREEN
import com.c4entertainment.moviehunter.util.Constants.FORGOT_PASSWORD_SCREEN
import com.c4entertainment.moviehunter.util.Constants.MOVIE_SCREEN
import com.c4entertainment.moviehunter.util.Constants.SEARCH_SCREEN
import com.c4entertainment.moviehunter.util.Constants.SIGN_IN_SCREEN
import com.c4entertainment.moviehunter.util.Constants.SIGN_UP_SCREEN
import com.c4entertainment.moviehunter.util.Constants.VERIFY_EMAIL_SCREEN

sealed class Screen(val route: String) {
    object AuthCheckScreen: Screen(AUTH_CHECK_SCREEN)
    object SignInScreen: Screen(SIGN_IN_SCREEN)
    object ForgotPasswordScreen: Screen(FORGOT_PASSWORD_SCREEN)
    object SignUpScreen: Screen(SIGN_UP_SCREEN)

    object MovieScreen: Screen(MOVIE_SCREEN)

    object VerifyEmailScreen: Screen(VERIFY_EMAIL_SCREEN)

    object SearchScreen: Screen(SEARCH_SCREEN)

}
