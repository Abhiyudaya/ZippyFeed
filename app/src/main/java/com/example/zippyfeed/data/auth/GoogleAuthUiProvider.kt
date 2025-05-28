package com.example.zippyfeed.data.auth

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.zippyfeed.GoogleServerClientID
import com.example.zippyfeed.data.models.GoogleAccount
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthUiProvider {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU) // API 33+
    suspend fun signIn(
        activityContext: Context,
        credentialManager: CredentialManager
    ): GoogleAccount {
        val creds: Credential = credentialManager.getCredential(
            activityContext,
            getCredentialRequest()
        ).credential
        return handleCredentials(creds)
    }

    fun handleCredentials(creds: Credential): GoogleAccount {
        when {
            creds is CustomCredential && creds.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
//                val googleIdTokenCredential = creds as GoogleIdTokenCredential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(creds.data)
                Log.d("GoogleAuthUiProvider", "GoogleIdTokenCredential : $googleIdTokenCredential")
                return GoogleAccount(
                    token = googleIdTokenCredential.idToken,
                    displayName = googleIdTokenCredential.displayName ?: "",
                    profileImageUrl = googleIdTokenCredential.profilePictureUri.toString()
                )
            }

            else -> {
                throw IllegalStateException("Invalid credential type")
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCredentialRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(
                GetSignInWithGoogleOption.Builder(GoogleServerClientID).build()
            )
            .build()
    }
}
