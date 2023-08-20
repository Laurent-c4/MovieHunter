package com.frogtest.movieguru.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.frogtest.movieguru.R
import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.domain.repository.ReloadUserResponse
import com.frogtest.movieguru.domain.repository.RevokeAccessResponse
import com.frogtest.movieguru.domain.repository.SendEmailVerificationResponse
import com.frogtest.movieguru.domain.repository.SendPasswordResetEmailResponse
import com.frogtest.movieguru.domain.repository.SignInResponse
import com.frogtest.movieguru.domain.repository.SignUpResponse
import com.frogtest.movieguru.presentation.sign_in.SignInResult
import com.frogtest.movieguru.presentation.sign_in.UserProfile
import com.frogtest.movieguru.util.Resource
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth,
) : AuthRepository {
    override val currentUser get() = auth.currentUser

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    override suspend fun googleSignInIntentSender(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun googleSignInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val idToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserProfile(
                        id = user.uid,
                        name = user.displayName,
                        email = user.email,
                        photoUrl = user.photoUrl?.toString()
                    )
                },
                error = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(data = null, error = e.message)
        }
    }

    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(data = null, error = e.message)
        }
    }

    override fun getSignedInUser(): UserProfile? {
        val user = currentUser
        return user?.run {
            UserProfile(
                id = user.uid,
                name = user.displayName,
                email = user.email,
                photoUrl = user.photoUrl?.toString()
            )
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String
    ): SignUpResponse = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(message = e.message ?: "An unknown error occurred", data = null)
    }

    override suspend fun sendEmailVerification(): SendEmailVerificationResponse = try {
        auth.currentUser?.sendEmailVerification()?.await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(message = e.message ?: "An unknown error occurred", data = null)
    }

    override suspend fun firebaseSignInWithEmailAndPassword(
        email: String, password: String
    ): SignInResponse = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(message = e.message ?: "An unknown error occurred", data = null)
    }

    override suspend fun reloadFirebaseUser(): ReloadUserResponse = try {
        auth.currentUser?.reload()?.await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(message = e.message ?: "An unknown error occurred", data = null)
    }

    override suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse = try {
        auth.sendPasswordResetEmail(email).await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(message = e.message ?: "An unknown error occurred", data = null)
    }

    override suspend fun revokeAccess(): RevokeAccessResponse = try {
        auth.currentUser?.delete()?.await()
        Resource.Success(true)
    } catch (e: Exception) {
        Resource.Error(message = e.message ?: "An unknown error occurred", data = null)
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)

}