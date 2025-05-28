package com.example.zippyfeed.ui.features.auth.login

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.zippyfeed.data.FoodApi
import com.example.zippyfeed.data.models.SignInRequest
import com.example.zippyfeed.ui.features.auth.BaseAuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(override val foodApi: FoodApi) : BaseAuthViewModel(foodApi) {


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
            try {
                val response = foodApi.signIn(
                    SignInRequest(
                        email = email.value,
                        password = password.value
                    )
                )
                if (response.body()?.token?.isNotEmpty()==true) {
                    _uiState.value = SignInEvent.Success
                    _navigationEvent.emit(SignInNavigationEvent.NavigationToHome)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SignInEvent.Error
                Log.e("SignUpError", "Error signing up", e)
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
            _uiState.value = SignInEvent.Error
            Log.e("GoogleError", message)
        }
    }

    override fun onFacebookError(message: String) {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Error
            Log.e("FacebookError", message)
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            _uiState.value = SignInEvent.Success
            _navigationEvent.emit(SignInNavigationEvent.NavigationToHome)
        }
    }
}