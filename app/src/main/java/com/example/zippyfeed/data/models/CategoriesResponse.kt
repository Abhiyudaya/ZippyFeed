package com.example.zippyfeed.data.models


import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("data")
    val `data`: List<Category>
)