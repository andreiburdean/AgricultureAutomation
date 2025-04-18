package com.example.agricultureautomationapp.apiservices

import com.example.agricultureautomationapp.models.ControlStatus
import com.example.agricultureautomationapp.models.EnvironmentItem
import java.lang.Void
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EnvironmentApiService {
    @GET("/api/environment/{userId}/get-environments")
    fun getEnvironments(@Path("userId") userId: Int): Call<List<EnvironmentItem>>

    @POST("/api/environment/{userId}/add-environment")
    fun addEnvironment(@Path("userId") userId: Int, @Body environment: EnvironmentItem): Call<EnvironmentItem>

    @DELETE("/api/environment/{userId}/delete-environment/{environmentId}")
    fun deleteEnvironment(@Path("userId") userId: Int, @Path("environmentId") environmentId: Int): Call<Void>

    @GET("/api/environment/{environmentId}/get-control-status")
    fun getControlStatus(@Path("environmentId") envId: Int): Call<ControlStatus>

    @POST("/api/environment/{environmentId}/control-environment")
    fun controlEnvironment(@Path("environmentId") envId: Int, @Body status: ControlStatus): Call<ControlStatus>
}