package com.example.agricultureautomationapp.apiservices

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("/api/users/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/api/users/create-user")
    fun registerUser(@Body request: LoginRequest): Call<ResponseBody>
}
