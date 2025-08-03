package com.example.zippyfeed.ui.features.auth.login

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.zippyfeed.data.FoodApi
import com.example.zippyfeed.data.ZippyFeedSession
import com.example.zippyfeed.data.models.SignInRequest
import com.example.zippyfeed.data.remote.ApiResponse
import com.example.zippyfeed.data.remote.safeApiCall
import com.example.zippyfeed.ui.features.auth.AuthScreenViewModel.AuthEvent
import com.example.zippyfeed.ui.features.auth.BaseAuthViewModel
import com.example.zippyfeed.ui.features.auth.signup.SignUpViewModel.SignUpEvent
import com.example.zippyfeed.ui.features.auth.signup.SignUpViewModel.SignUpNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(override val foodApi: FoodApi, val session: ZippyFeedSession) :
    BaseAuthViewModel(foodApi) {


    private val _uiState = MutableStateFlow<SignInEvent>(SignInEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignInNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()


    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignInClick() {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Loading
            val response = safeApiCall {
                foodApi.signIn(
                    SignInRequest(
                        email.value, password.value
                    )
                )
            }
            when (response) {
                is ApiResponse.Success -> {
                    _uiState.value = SignInEvent.Success
                    session.storeToken(response.data.token)
                    _navigationEvent.emit(SignInNavigationEvent.NavigationToHome)
                }
                else -> {
                    val errr = (response as? ApiResponse.Error)?.code ?:0
                    error = "Sign In Failed"
                    errorDescription = "Failed to sign up"
                    when(errr) {
                        400 -> {
                            errorDescription = "Please enter correct details"
                            error = "Invalid Credentials"
                        }
                    }
                    _uiState.value = SignInEvent.Error
                }
            }
        }

    }


    fun onSignUpClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SignInNavigationEvent.NavigationToSignUp)
        }
    }

    sealed class SignInNavigationEvent {
        object NavigationToSignUp : SignInNavigationEvent()
        object NavigationToHome : SignInNavigationEvent()
    }

    sealed class SignInEvent {
        object Nothing : SignInEvent()
        object Success : SignInEvent()
        object Error : SignInEvent()
        object Loading : SignInEvent()
    }

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Loading
        }
    }

    override fun onGoogleError(message: String) {
        viewModelScope.launch {
            errorDescription = message
            error = "Google Sign In Failed "
            _uiState.value = SignInEvent.Error
            Log.e("GoogleError", message)
            _navigationEvent.emit(SignInNavigationEvent.NavigationToHome)
        }
    }

    override fun onFacebookError(message: String) {
        viewModelScope.launch {
            errorDescription = message
            error = "Facebook Sign In Failed "
            _uiState.value = SignInEvent.Error
            Log.e("FacebookError", message)
            _navigationEvent.emit(SignInNavigationEvent.NavigationToHome)
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            session.storeToken(token)
            _uiState.value = SignInEvent.Success
            _navigationEvent.emit(SignInNavigationEvent.NavigationToHome)
        }
    }
}