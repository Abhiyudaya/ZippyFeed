package com.example.zippyfeed.ui.features.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.zippyfeed.data.FoodApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(override val foodApi: FoodApi) : BaseAuthViewModel(foodApi) {


    private val _uiState = MutableStateFlow<AuthEvent>(AuthEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AuthInNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    sealed class AuthInNavigationEvent {
        object NavigationToSignUp : AuthInNavigationEvent()
        object NavigationToHome : AuthInNavigationEvent()
        object ShowErrorDialog : AuthInNavigationEvent()
    }

    sealed class AuthEvent {
        object Nothing : AuthEvent()
        object Success : AuthEvent()
        object Error : AuthEvent()
        object Loading : AuthEvent()
    }

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = AuthEvent.Loading
        }
    }

    override fun onGoogleError(message: String) {
        viewModelScope.launch {
            errorDescription = message
            error = "Google Sign In Failed "
            _uiState.value = AuthEvent.Error
            Log.e("GoogleError", message)
            _navigationEvent.emit(AuthInNavigationEvent.NavigationToHome)
        }
    }

    override fun onFacebookError(message: String) {
        viewModelScope.launch {
            errorDescription = message
            error = "Facebook Sign In Failed "
            _uiState.value = AuthEvent.Error
            Log.e("FacebookError", message)
            _navigationEvent.emit(AuthInNavigationEvent.NavigationToHome)
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            _uiState.value = AuthEvent.Success
            _navigationEvent.emit(AuthInNavigationEvent.NavigationToHome)
        }
    }
}