package com.example.agricultureautomationapp.apiservices

import com.example.agricultureautomationapp.models.ProgramItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProgramApiService {
    @GET("/api/program/{environmentId}/get-programs")
    fun getPrograms(@Path("environmentId") environmentId: Int): Call<List<ProgramItem>>

    @POST("/api/program/{environmentId}/add-program")
    fun addProgram(@Path("environmentId") environmentId: Int, @Body program: ProgramItem): Call<ProgramItem>

    @PUT("/api/program/{programId}/update-program")
    fun updateProgram(@Path("programId") programId: Int, @Body program: ProgramItem): Call<ProgramItem>

    @DELETE("/api/program/{programId}/delete-program")
    fun deleteProgram(@Path("programId") programId: Int): Call<Void>

    @POST("/api/program/{environmentId}/{programId}/start-program")
    fun startProgram(@Path("environmentId") environmentId: Int, @Path("programId") programId: Int): Call<Boolean>

    @POST("/api/program/{programId}/stop-program")
    fun stopProgram(@Path("programId") programId: Int): Call<Boolean>
}