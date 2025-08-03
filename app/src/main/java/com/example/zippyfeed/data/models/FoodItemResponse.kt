package com.example.zippyfeed.data.models


import com.google.gson.annotations.SerializedName

data class FoodItemResponse(
    @SerializedName("foodItems")
    val foodItems: List<FoodItem?>?
)