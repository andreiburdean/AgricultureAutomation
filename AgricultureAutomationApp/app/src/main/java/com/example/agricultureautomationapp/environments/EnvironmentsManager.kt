package com.example.agricultureautomationapp.environments

import android.content.ContentValues.TAG
import android.content.Context
import com.example.agricultureautomationapp.models.EnvironmentItem

import android.util.Log
import android.widget.Toast
import com.example.agricultureautomationapp.apiservices.EnvironmentApiService
import com.example.agricultureautomationapp.sharedpreferences.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EnvironmentsManager(private val context: Context) {
    private val environmentsList = mutableListOf<EnvironmentItem>()

    private val BASE_URL = "http://10.0.2.2:8080";
//    private val BASE_URL = "http://192.168.40.113:8080";

    private fun getApiService(): EnvironmentApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(EnvironmentApiService::class.java)
    }

    fun fetchEnvironments(onResult: (List<EnvironmentItem>) -> Unit) {
        val apiService = getApiService()
        val userId = SharedPreferences.getUserId(context)
        Log.d("EnvironmentsManager", "${userId}")
        val call = apiService.getEnvironments(userId)

        call.enqueue(object : Callback<List<EnvironmentItem>> {
            override fun onResponse(
                call: Call<List<EnvironmentItem>>,
                response: Response<List<EnvironmentItem>>
            ) {
                if (response.isSuccessful) {
                    environmentsList.clear()
                    response.body()?.let {
                        environmentsList.addAll(it)
                    }
                    Log.d("EnvironmentsManager", "Environments list: $environmentsList")
                    onResult(environmentsList)
                } else {
                    Log.d("EnvironmentsManager", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<EnvironmentItem>>, t: Throwable) {
                Log.d("EnvironmentsManager", "API call failed: ${t.message}")
            }
        })
    }

    fun addEnvironment(environment: EnvironmentItem, onResult: (List<EnvironmentItem>) -> Unit) {
        val apiService = getApiService()
        val userId = SharedPreferences.getUserId(context)

        apiService.addEnvironment(userId, environment).enqueue(object : Callback<EnvironmentItem> {
            override fun onResponse(call: Call<EnvironmentItem>, response: Response<EnvironmentItem>) {
                when (response.code()) {
                    201 -> {
                        response.body()?.let {
                            environmentsList.add(it)
                        }
                        onResult(environmentsList)
                    }
                    400 -> {
                        Log.d(TAG, "User creation failure: " + response.code())
                    }
                    409 -> {
                        Toast.makeText(context,
                            "Raspberry id already used by another environment", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<EnvironmentItem>, t: Throwable) {
                Log.e("EnvironmentManager", "Failed to add environment: ${t.message}")
            }
        })
    }

    fun deleteEnvironment(environment: EnvironmentItem, onResult: (Boolean) -> Unit) {
        val apiService = getApiService()
        val userId = SharedPreferences.getUserId(context)

        val call = apiService.deleteEnvironment(userId, environment.environmentId ?: 0)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("EnvironmentsManager", "Environment deleted successfully")
                    environmentsList.remove(environment)
                    onResult(true)
                } else {
                    Log.e("EnvironmentsManager", "Failed to delete environment: ${response.code()}")
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("EnvironmentsManager", "Error deleting environment: ${t.message}")
                onResult(false)
            }
        })
    }
}

