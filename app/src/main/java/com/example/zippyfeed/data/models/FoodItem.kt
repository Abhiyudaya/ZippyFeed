package com.example.zippyfeed.data.models

import com.google.gson.annotations.SerializedName

data class FoodItem(
    @SerializedName("arModelUrl")
    val arModelUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("restaurantId")
    val restaurantId: String?
)
