package com.example.zippyfeed.data

import android.content.Context
import android.content.SharedPreferences

class ZippyFeedSession(private val context: Context) {
    val sharedPreferences : SharedPreferences = context.getSharedPreferences("ZippyFeed", Context.MODE_PRIVATE)
    init{

    }

    fun storeToken(token : String){
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun getToken() :  String?{
        sharedPreferences.getString("token", null)?.let{
            return it
        }
        return null
    }

}