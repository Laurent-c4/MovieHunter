package com.frogtest.movieguru.data.repository

import com.frogtest.movieguru.domain.repository.AuthRepository
import com.frogtest.movieguru.domain.repository.ReloadUserResponse
import com.frogtest.movieguru.domain.repository.RevokeAccessResponse
import com.frogtest.movieguru.domain.repository.SendEmailVerificationResponse
import com.frogtest.movieguru.domain.repository.SendPasswordResetEmailResponse
import com.frogtest.movieguru.domain.repository.SignInResponse
import com.frogtest.movieguru.domain.repository.SignUpResponse
import com.frogtest.movieguru.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override val currentUser get() = auth.currentUser

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

    override fun signOut() = auth.signOut()

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