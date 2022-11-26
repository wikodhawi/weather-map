package com.dhabasoft.weathermap.core

import android.content.Context
import android.content.Intent
import com.dhabasoft.weathermap.view.login.LoginActivity

/**
 * Created by dhaba
 */
class MyPreferences(private val context: Context) {

    companion object {
        private const val FILE_NAME = "preferences"
        private const val KEY_TOKEN = "token"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_LATITUDE = "location"
        private const val KEY_LONGITUDE = "longitude"
    }

    private var sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setToken(token: String) {
        editor.putString(KEY_TOKEN, "Bearer $token")
        editor.apply()
    }

    fun setLatitude(latitude: String) {
        editor.putString(KEY_LATITUDE, latitude)
        editor.apply()
    }

    fun setLongitude(longitude: String) {
        editor.putString(KEY_LONGITUDE, longitude)
        editor.apply()
    }

    fun setLanguage(language: String) {
        editor.putString(KEY_LANGUAGE, language)
        editor.apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, "") ?: ""
    }

    fun getLatitude(): String {
        val latitude = sharedPreferences.getString(KEY_LATITUDE, "")
        return if (latitude.isNullOrBlank()) "0.0" else latitude
    }

    fun getLongitude(): String {
        val longitude = sharedPreferences.getString(KEY_LONGITUDE, "")
        return if (longitude.isNullOrBlank()) "0.0" else longitude
    }

    fun getToken(): String {
        return sharedPreferences.getString(KEY_TOKEN, "") ?: ""
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val i = Intent(context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(i)
    }
}