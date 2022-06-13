package com.example.suicidepreventiveapp.data.preferences

import android.content.Context

internal class UserPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value : UserModel) {
        val editor = preferences.edit()
        editor.putString(USERNAME, value.username)
        editor.putString(PHONE, value.phone)
        editor.putString(EMAIL, value.email)
        editor.putString(ADDRESS, value.address)
        editor.putString(ACCESS_TOKEN, value.accessToken)
        editor.putString(REFRESH_TOKEN, value.refreshToken)
        editor.apply()
    }

    fun setRefreshToken(token: String) {
        val editor = preferences.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
    }

    fun getUser() : UserModel {
        val model = UserModel()
        model.username = preferences.getString(USERNAME, "")
        model.phone = preferences.getString(PHONE, "")
        model.email = preferences.getString(EMAIL, "")
        model.address = preferences.getString(ADDRESS, "")
        model.accessToken = preferences.getString(ACCESS_TOKEN, "")
        model.refreshToken = preferences.getString(REFRESH_TOKEN, "")

        return model
    }

    fun clearUser() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val USERNAME = "name"
        private const val PHONE = "phone"
        private const val EMAIL = "email"
        private const val ADDRESS = "address"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}