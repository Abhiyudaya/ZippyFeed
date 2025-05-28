package com.example.zippyfeed.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesRetrofit() : Retrofit{
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.213.147:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    fun provideFoodApi(retrofit: Retrofit): FoodApi{
        return retrofit.create(FoodApi::class.java)
    }
}