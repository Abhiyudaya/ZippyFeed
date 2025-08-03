package com.example.zippyfeed.data.models


import com.google.gson.annotations.SerializedName

data class RestaurantResponse(
    @SerializedName("data")
    val `data`: List<Restaurant?>?
)