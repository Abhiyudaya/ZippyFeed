package com.example.zippyfeed.ui.features.restaurant_details

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zippyfeed.data.FoodApi
import com.example.zippyfeed.data.models.FoodItem
import com.example.zippyfeed.data.remote.ApiResponse
import com.example.zippyfeed.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(val foodApi: FoodApi): ViewModel() {

    var errorMsg = ""
    var errorDescription = ""

    private val _uiState = MutableStateFlow<RestaurantEvent>(RestaurantEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RestaurantNavigationEvent?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun getFoodItem(restaurantId : String){
        viewModelScope.launch {
            _uiState.value = RestaurantEvent.Loading
            e("RestaurantViewModel", "Fetching food items for restaurant ID: $restaurantId")
            try {
                val response = safeApiCall {
                    foodApi.getFoodItemForRestaurants(restaurantId)
                }
                e("RestaurantViewModel", "API Response: $response")
                when(response){
                    is ApiResponse.Success -> {
                        val foodItems = response.data.foodItems?.filterNotNull() ?: emptyList()
                        e("RestaurantViewModel", "Success: Found ${foodItems.size} food items")
                        _uiState.value = RestaurantEvent.Success(foodItems)
                    }
                    else -> {
                        val error = (response as? ApiResponse.Error)?.code
                        e("RestaurantViewModel", "API Error - Code: $error, Response: $response")
                        when(error){
                            401 -> {
                                errorMsg = "Unauthorized"
                                errorDescription = "You are not authorized to perform this action"
                            }
                            404 -> {
                                errorMsg = "Not Found"
                                errorDescription = "The Restaurant was not found"
                            }
                            else -> {
                                errorMsg = "Error"
                                errorDescription = "Something went wrong"
                            }
                        }
                        _uiState.value = RestaurantEvent.Error
                        _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
                    }
                }
            }
            catch(e : Exception) {
                e("RestaurantViewModel", "Exception: ${e.message}", e)
                _uiState.value = RestaurantEvent.Error
                _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
            }
        }
    }


    sealed class RestaurantNavigationEvent{
        data object GoBack : RestaurantNavigationEvent()
        data object ShowErrorDialog : RestaurantNavigationEvent()
        data class NavigateToProductDetails(val productId : String) : RestaurantNavigationEvent()
    }

    sealed class RestaurantEvent{
        data object Loading : RestaurantEvent()
        data object Nothing : RestaurantEvent()
        data class Success(val foodItems: List<FoodItem>) : RestaurantEvent()
        data object Error : RestaurantEvent()
    }
}