package com.example.zippyfeed.ui.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zippyfeed.data.FoodApi
import com.example.zippyfeed.data.models.Category
import com.example.zippyfeed.data.models.Restaurant
import com.example.zippyfeed.data.remote.ApiResponse
import com.example.zippyfeed.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@HiltViewModel
class HomeViewModel @Inject constructor(private val foodApi: FoodApi) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<HomeScreenNavigationEvents?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    var categories by mutableStateOf(emptyList<Category>())
        private set
    var restaurants by mutableStateOf(emptyList<Restaurant>())
        private set

    init {
        viewModelScope.launch {
            fetchCategories()
            fetchRestaurants()
        }
    }

    private suspend fun fetchCategories() {
        val response = safeApiCall { foodApi.getCategories() }
        when (response) {
            is ApiResponse.Success -> {
                val data = response.data.data
                Log.d("HomeViewModel", "Categories: $data")
                if (data.isEmpty()) {
                    Log.w("HomeViewModel", "Categories list is empty")
                    _uiState.value = HomeScreenState.Empty
                } else {
                    categories = data
                    _uiState.value = HomeScreenState.Success
                }
            }
            is ApiResponse.Error -> {
                Log.e("HomeViewModel", "Categories API Error: ${response.code} ${response.message}")
                _uiState.value = HomeScreenState.Empty
            }
            is ApiResponse.Exception -> {
                Log.e("HomeViewModel", "Categories API Exception: ${response.exception}")
                _uiState.value = HomeScreenState.Empty
            }
        }
    }

    private suspend fun fetchRestaurants() {
        val response = safeApiCall { foodApi.getRestaurants(40.7128, -74.0060) }
        when (response) {
            is ApiResponse.Success -> {
                val data = response.data.data
                Log.d("HomeViewModel", "Restaurants: $data")
                if (data.isNullOrEmpty()) {
                    Log.w("HomeViewModel", "Restaurants list is empty")
                } else {
                    restaurants = data.filterNotNull()
                }
            }
            is ApiResponse.Error -> {
                Log.e("HomeViewModel", "Restaurants API Error: ${response.code} ${response.message}")
            }
            is ApiResponse.Exception -> {
                Log.e("HomeViewModel", "Restaurants API Exception: ${response.exception}")
            }
        }
    }

    fun onRestaurantSelected(restaurant: Restaurant) {
        viewModelScope.launch {
            _navigationEvent.emit(
                HomeScreenNavigationEvents.NavigateToDetails(
                    restaurant.name ?: "", restaurant.imageUrl ?: "", restaurant.id ?: ""))
        }
    }

    sealed class HomeScreenState{
        object Loading : HomeScreenState()
        object Success : HomeScreenState()
        object Empty : HomeScreenState()
    }

    sealed class HomeScreenNavigationEvents{
        data class NavigateToDetails(val name : String,val imageUrl : String,val id : String) : HomeScreenNavigationEvents()
    }

}