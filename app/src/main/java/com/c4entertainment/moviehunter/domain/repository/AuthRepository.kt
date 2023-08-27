package com.c4entertainment.moviehunter.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.c4entertainment.moviehunter.presentation.sign_in.SignInResult
import com.c4entertainment.moviehunter.presentation.sign_in.UserProfile
import com.c4entertainment.moviehunter.util.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow


typealias SignUpResponse = Resource<Boolean>
typealias SendEmailVerificationResponse = Resource<Boolean>
typealias SignInResponse = Resource<Boolean>
typealias ReloadUserResponse = Resource<Boolean>
typealias SendPasswordResetEmailResponse = Resource<Boolean>
typealias RevokeAccessResponse = Resource<Boolean>
typealias AuthStateResponse = StateFlow<Boolean>

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun googleSignInIntentSender(): IntentSender?

    suspend fun googleSignInWithIntent(intent: Intent): SignInResult

    fun getSignedInUser(): UserProfile?

    suspend fun firebaseSignUpWithEmailAndPassword(email: String, password: String): SignUpResponse

    suspend fun sendEmailVerification(): SendEmailVerificationResponse

    suspend fun firebaseSignInWithEmailAndPassword(email: String, password: String): SignInResponse

    suspend fun reloadFirebaseUser(): ReloadUserResponse

    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse

    suspend fun signOut()

    suspend fun revokeAccess(): RevokeAccessResponse

    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse
}