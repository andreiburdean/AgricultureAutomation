package com.example.agricultureautomationapp.programs

import android.content.Context
import android.util.Log

import com.example.agricultureautomationapp.apiservices.EnvironmentApiService
import com.example.agricultureautomationapp.apiservices.ProgramApiService
import com.example.agricultureautomationapp.models.ProgramItem
import com.example.agricultureautomationapp.sharedpreferences.SharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProgramsManager(private val context: Context) {
    private val programsList = mutableListOf<ProgramItem>()
    private val BASE_URL = "http://10.0.2.2:8080";

    private fun getApiService(): ProgramApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ProgramApiService::class.java)
    }

    fun fetchPrograms(onResult: (List<ProgramItem>) -> Unit) {
        val apiService = getApiService()
        val environmentId = SharedPreferences.getEnvironmentId(context)
        Log.d("ProgramsManager", "$environmentId")
        val call = apiService.getPrograms(environmentId)

        call.enqueue(object : Callback<List<ProgramItem>> {
            override fun onResponse(
                call: Call<List<ProgramItem>>,
                response: Response<List<ProgramItem>>
            ) {
                if (response.isSuccessful) {
                    programsList.clear()
                    response.body()?.let {
                        programsList.addAll(it)
                    }
                    Log.d("ProgramsManager", "Programs list: $programsList.")
                    onResult(programsList)
                } else {
                    Log.d("ProgramsManager", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ProgramItem>>, t: Throwable) {
                Log.d("ProgramsManager", "API call failed: ${t.message}")
            }
        })
    }
//
//    fun addEnvironment(environment: EnvironmentItem, onResult: (List<EnvironmentItem>) -> Unit) {
//        val apiService = getApiService()
//        val userId = SharedPreferences.getUserId(context)
//
//        apiService.addEnvironment(userId, environment).enqueue(object : Callback<EnvironmentItem> {
//            override fun onResponse(call: Call<EnvironmentItem>, response: Response<EnvironmentItem>
//            ) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        environmentsList.add(it)
//                    }
//                    onResult(environmentsList)
//                } else {
//                    Log.e("EnvironmentManager", "Failed to add environment: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<EnvironmentItem>, t: Throwable) {
//                Log.e("EnvironmentManager", "Failed to add environment: ${t.message}")
//            }
//        })
//    }
//
//    fun deleteProgram(program: ProgramItem, onResult: (Boolean) -> Unit) {
//        val apiService = getApiService()
//        val userId = SharedPreferences.getUserId(context)
//
//        val call = apiService.deleteEnvironment(userId, environment.environmentId ?: 0)
//
//        call.enqueue(object : Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    Log.d("EnvironmentsManager", "Environment deleted successfully")
//                    environmentsList.remove(environment)
//                    onResult(true)
//                } else {
//                    Log.e("EnvironmentsManager", "Failed to delete environment: ${response.code()}")
//                    onResult(false)
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                Log.e("EnvironmentsManager", "Error deleting environment: ${t.message}")
//                onResult(false)
//            }
//        })
//    }
}

