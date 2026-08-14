package com.example.mmazone.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthManager(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val WEB_CLIENT_ID = "541017329242-p750b8jasg7sd2okde6t1f4e6fejimb3.apps.googleusercontent.com"

    suspend fun signInWithGoogle(): Boolean {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

                firebaseAuth.signInWithCredential(firebaseCredential).await()
                true
            } else {
                false
            }
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun signInWithEmail(email: String, pass: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            val user = result.user

            if (user != null) {
                if (user.isEmailVerified) {
                    AuthResult.Success
                } else {
                    firebaseAuth.signOut()
                    AuthResult.Error("Please verify your email address before logging in.")
                }
            } else {
                AuthResult.Error("An unknown error occurred.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResult.Error("Invalid email or password.")
        }
    }

    suspend fun signUpWithEmail(email: String, pass: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user

            if (user != null) {
                user.sendEmailVerification().await()
                firebaseAuth.signOut()

                AuthResult.VerificationSent
            } else {
                AuthResult.Error("Could not create user account.")
            }

        } catch (e: com.google.firebase.auth.FirebaseAuthWeakPasswordException) {
            AuthResult.Error("Password is too weak (minimum 6 characters).")
        } catch (e: com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error("The email address is invalid.")
        } catch (e: com.google.firebase.auth.FirebaseAuthUserCollisionException) {
            AuthResult.Error("An account already exists with this email.")
        } catch (e: Exception) {
            e.printStackTrace()
            AuthResult.Error(e.localizedMessage ?: "Registration failed.")
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    sealed class AuthResult {
        object Success : AuthResult()
        object VerificationSent : AuthResult()
        data class Error(val message: String) : AuthResult()
    }
}