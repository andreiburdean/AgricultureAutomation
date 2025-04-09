package com.example.agricultureautomationapp.sharedpreferences
import android.content.Context

class SharedPreferences {
    companion object {
        private const val PREFS_NAME = "UserPrefs"
        private const val USER_ID_KEY = "user_id"
        private const val ENV_ID_KEY = "environment_id"
        private const val ENV_NAME_KEY = "environment_name"

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

        fun saveEnvironmentId(context: Context, environmentId: Int) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(ENV_ID_KEY, environmentId)
            editor.apply()
        }

        fun getEnvironmentId(context: Context): Int {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(ENV_ID_KEY, 0)
        }

        fun saveEnvironmentName(context: Context, environmentName: String) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(ENV_NAME_KEY, environmentName)
            editor.apply()
        }

        fun getEnvironmentName(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(ENV_NAME_KEY, "")!!
        }
    }
}
