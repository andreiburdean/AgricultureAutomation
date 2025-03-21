package com.example.agricultureautomationapp.apiservices

import com.example.agricultureautomationapp.models.ProgramItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProgramApiService {
    @GET("/api/program/{environmentId}/get-programs/")
    fun getPrograms(@Path("environmentId") environmentId: Int): Call<List<ProgramItem>>

    @POST("/api/program/{userId}/add-program/")
    fun addProgram(@Path("userId") userId: Int, @Body program: ProgramItem): Call<ProgramItem>

    @DELETE("/api/program/{userId}/delete-program/{programId}")
    fun deleteProgram(@Path("userId") userId: Int, @Path("programId") programId: Int): Call<Void>
}