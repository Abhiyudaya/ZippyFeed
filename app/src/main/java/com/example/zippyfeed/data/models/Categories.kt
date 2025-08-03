package com.example.zippyfeed.data.models


import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("data")
    val `data`: List<Category?>?
)