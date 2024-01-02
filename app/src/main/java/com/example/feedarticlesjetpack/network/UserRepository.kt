package com.example.feedarticlesjetpack.network

import android.content.SharedPreferences
import javax.inject.Inject

class UserRepository  @Inject constructor(private val sharedPreferences: SharedPreferences){

    fun saveUserId(userId: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("userId", userId)
        editor.apply()
    }

    fun getUserIdFromPreferences(): Long {
        return sharedPreferences.getLong("userId", 0L)
    }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getTokenFromPreferences(): String? {
        return sharedPreferences.getString("token", null)
    }
}
