package com.example.zippyfeed.data.remote

import retrofit2.Response


sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error(val code:Int, val message : String) : ApiResponse<Nothing>(){
        fun formatMsg(): String {
            return "Error: $code $message"
        }
    }
    data class Exception(val exception: kotlin.Exception) : ApiResponse<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val res = apiCall.invoke()
        if (!res.isSuccessful) {
            ApiResponse.Error(res.code(), res.message())
        } else  {
            ApiResponse.Success(res.body()!!)
        }
    } catch (e: Exception) {
        ApiResponse.Exception(e)
    }
}