package com.example.agricultureautomationapp.apiservices

import com.example.agricultureautomationapp.models.SensorData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MonitorApiService {
    @GET("/api/rpi/{environmentId}/get-sensor-data")
    fun getSensorDataByEnvironment(@Path("environmentId") environmentId: Int): Call<SensorData>
}