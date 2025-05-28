package com.example.zippyfeed.ui.features.auth

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippyfeed.data.FoodApi
import com.example.zippyfeed.data.auth.GoogleAuthUiProvider
import com.example.zippyfeed.data.models.OAuthRequest
import com.example.zippyfeed.data.remote.ApiResponse
import com.example.zippyfeed.data.remote.safeApiCall
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel(open val foodApi: FoodApi) : ViewModel() {

    var error : String = ""
    var errorDescription: String = ""

    private val googleAuthUiProvider = GoogleAuthUiProvider()
    private lateinit var callbackManager: CallbackManager

    abstract fun loading()
    abstract fun onGoogleError(message: String)
    abstract fun onFacebookError(message: String)
    abstract fun onSocialLoginSuccess(token: String)

    fun onFacebookClicked(context: ComponentActivity) {
        initiateFacebookLogin(context)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onGoogleClicked(context: ComponentActivity) {
        initiateGoogleSignIn(context)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    protected fun initiateGoogleSignIn(context: ComponentActivity) {
        viewModelScope.launch {
            loading()
            try{
                val response = googleAuthUiProvider.signIn(
                    context,
                    CredentialManager.create(context)
                )
                fetchZippyFeedToken(response.token, "google"){
                    onGoogleError(it)
                }
            }catch (e: Throwable) {
                onGoogleError(e.message.toString())
            }
        }
    }

    private fun fetchZippyFeedToken(token: String, provider: String, onError:(String)-> Unit) {
        viewModelScope.launch {
            try {
                val request = OAuthRequest(
                    token = token,
                    provider = provider
                )
                val res = safeApiCall { foodApi.oAuth(request) }
                when (res) {
                    is ApiResponse.Success-> {
                        onSocialLoginSuccess(res.data.token)
                    }
                    else -> {
                        val error = (res as? ApiResponse.Error)?.code
                        if (error!= null) {
                            when (error) {
                                401 -> onError("Invalid token")
                                500 -> onError("Server error")
                                404 -> onError("Not found")
                                else -> onError("Failed")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                onError("Failed to fetch token: ${e.message}")
            }
        }
    }

    protected fun initiateFacebookLogin(context: ComponentActivity) {
        loading()
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    fetchZippyFeedToken(loginResult.accessToken.token, "facebook") {
                        onFacebookError(it)
                    }
                }

                override fun onCancel() {
                    onFacebookError("Cancelled")
                }

                override fun onError(exception: FacebookException) {
                    onFacebookError(exception.message?: "Unknown error")
                }
            })

        LoginManager.getInstance().logInWithReadPermissions(
            context,
            callbackManager,
            listOf("public_profile", "email")
        )
    }
}
