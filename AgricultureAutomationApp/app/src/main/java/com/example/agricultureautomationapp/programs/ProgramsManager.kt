package com.example.agricultureautomationapp.programs

import android.content.Context
import android.util.Log

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

    fun addProgram(program: ProgramItem, onResult: (List<ProgramItem>) -> Unit) {
        val apiService = getApiService()
        val environmentId = SharedPreferences.getEnvironmentId(context)

        apiService.addProgram(environmentId, program).enqueue(object : Callback<ProgramItem> {
            override fun onResponse(call: Call<ProgramItem>, response: Response<ProgramItem>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        programsList.add(it)
                    }
                    onResult(programsList)
                } else {
                    Log.e("EnvironmentManager", "Failed to add environment: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProgramItem>, t: Throwable) {
                Log.e("EnvironmentManager", "Failed to add environment: ${t.message}")
            }
        })
    }

    fun deleteProgram(program: ProgramItem, onResult: (Boolean) -> Unit) {
        val apiService = getApiService()
        val call = apiService.deleteProgram(program.programId!!)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ProgramsManager", "Program deleted successfully")
                    programsList.remove(program)
                    onResult(true)
                } else {
                    Log.e("ProgramsManager", "Failed to delete program: ${response.code()}")
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ProgramsManager", "Error deleting program: ${t.message}")
                onResult(false)
            }
        })
    }

    fun startProgram(programId: Int, onResult: (Boolean) -> Unit) {
        val apiService = getApiService()
        val environmentId = SharedPreferences.getEnvironmentId(context)
        val call = apiService.startProgram(environmentId, programId)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    onResult(response.body() ?: true)
                } else {
                    Log.e("ProgramsManager", "Failed to start program: ${response.code()}")
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("ProgramsManager", "Error starting program: ${t.message}")
                onResult(false)
            }
        })
    }

    fun stopProgram(programId: Int, onResult: (Boolean) -> Unit) {
        val apiService = getApiService()
        val call = apiService.stopProgram(programId)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    onResult(response.body() ?: true)
                } else {
                    Log.e("ProgramsManager", "Failed to start program: ${response.code()}")
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("ProgramsManager", "Error starting program: ${t.message}")
                onResult(false)
            }
        })
    }
}

