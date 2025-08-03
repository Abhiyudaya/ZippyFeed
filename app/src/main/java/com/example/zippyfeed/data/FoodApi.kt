package com.example.zippyfeed.data

import com.example.zippyfeed.data.models.AuthResponse
import com.example.zippyfeed.data.models.CategoriesResponse
import com.example.zippyfeed.data.models.FoodItemResponse
import com.example.zippyfeed.data.models.OAuthRequest
import com.example.zippyfeed.data.models.RestaurantResponse
import com.example.zippyfeed.data.models.SignInRequest
import com.example.zippyfeed.data.models.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {
    @GET("/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("/restaurants")
    suspend fun getRestaurants(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double
    ): Response<RestaurantResponse>

    @GET("/restaurants/{restaurantId}/menu")
    suspend fun getFoodItemForRestaurants(@Path("restaurantId") restaurantId : String): Response<FoodItemResponse>

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest):Response<AuthResponse>

    @POST("/auth/login")
    suspend fun signIn(@Body request: SignInRequest):Response<AuthResponse>

    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest): Response<AuthResponse>
}