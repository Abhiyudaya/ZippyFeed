package com.example.zippyfeed.ui.features.auth.signup

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippyfeed.ZippyFeedApp
import com.example.zippyfeed.data.FoodApi
import com.example.zippyfeed.data.ZippyFeedSession
import com.example.zippyfeed.data.models.SignUpRequest
import com.example.zippyfeed.data.remote.ApiResponse
import com.example.zippyfeed.data.remote.safeApiCall
import com.example.zippyfeed.ui.features.auth.AuthScreenViewModel.AuthEvent
import com.example.zippyfeed.ui.features.auth.BaseAuthViewModel
import com.example.zippyfeed.ui.features.auth.login.SignInViewModel.SignInEvent
import com.example.zippyfeed.ui.features.auth.login.SignInViewModel.SignInNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(override val foodApi: FoodApi, val session : ZippyFeedSession) :
    BaseAuthViewModel(foodApi) {
    private val _uiState = MutableStateFlow<SignUpEvent>(SignUpEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignUpNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Loading
            try {
                val response = safeApiCall {
                    foodApi.signUp(
                        SignUpRequest(
                            name = name.value,
                            email = email.value,
                            password = password.value
                        )
                    )
                }
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.value = SignUpEvent.Success
                        session.storeToken(response.data.token)
                        _navigationEvent.emit(SignUpNavigationEvent.NavigationToHome)
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
                        _uiState.value = SignUpEvent.Error
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SignUpEvent.Error
                Log.e("SignUpError", "Error signing up", e)
            }
        }

    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SignUpNavigationEvent.NavigationToLogin)
        }
    }

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Loading
        }
    }

    override fun onGoogleError(message: String) {
        viewModelScope.launch {
            errorDescription = message
            error = "Google Sign In Failed "
            _uiState.value = SignUpEvent.Error
            Log.e("GoogleError", message)
            _navigationEvent.emit(SignUpNavigationEvent.NavigationToHome)
        }
    }

    override fun onFacebookError(message: String) {
        viewModelScope.launch {
            errorDescription = message
            error = "Facebook Sign In Failed "
            _uiState.value = SignUpEvent.Error
            Log.e("FacebookError", message)
            _navigationEvent.emit(SignUpNavigationEvent.NavigationToHome)
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            session.storeToken(token)
            _uiState.value = SignUpEvent.Success
            _navigationEvent.emit(SignUpNavigationEvent.NavigationToHome)
        }
    }

    sealed class SignUpNavigationEvent {
        object NavigationToLogin : SignUpNavigationEvent()
        object NavigationToHome : SignUpNavigationEvent()
    }

    sealed class SignUpEvent {
        object Nothing : SignUpEvent()
        object Success : SignUpEvent()
        object Error : SignUpEvent()
        object Loading : SignUpEvent()
    }


}