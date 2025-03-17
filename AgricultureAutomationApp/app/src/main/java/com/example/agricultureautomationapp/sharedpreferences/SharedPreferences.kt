package com.example.agricultureautomationapp.sharedpreferences
import android.content.Context

class SharedPreferences {
    companion object {
        private const val PREFS_NAME = "UserPrefs"
        private const val USER_ID_KEY = "user_id"

        fun saveUserId(context: Context, userId: Int) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(USER_ID_KEY, userId)
            editor.apply()
        }

        fun getUserId(context: Context): Int {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(USER_ID_KEY, 0)
        }
    }
}
