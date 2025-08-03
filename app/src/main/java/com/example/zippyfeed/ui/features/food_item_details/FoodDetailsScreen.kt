package com.example.zippyfeed.ui.features.food_item_details

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.zippyfeed.data.models.FoodItem
import com.example.zippyfeed.ui.features.restaurant_details.RestaurantDetails
import com.example.zippyfeed.ui.features.restaurant_details.RestaurantDetailsHeader

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FoodDetailsScreen(
    foodItem: FoodItem,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController
){
    Column(modifier =  Modifier.fillMaxSize()) {
        RestaurantDetailsHeader(
            imageUrl = foodItem.imageUrl ?: "",
            restaurantId = foodItem.restaurantId ?: "",
            animatedVisibilityScope = animatedVisibilityScope,
            onBackClick = {}){
        }
        RestaurantDetails(
            title = foodItem.name ?: "",
            description = foodItem.description ?: "",
            restaurantId = foodItem.restaurantId ?: "",
            animatedVisibilityScope = animatedVisibilityScope
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ){
            Text(text = "$${foodItem.price}", color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}