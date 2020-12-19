package com.example.busticketactivity.firebase

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetStatus {
    @GET("{id}/status")

    fun getTransaksi(
        @Path("id") otp:String?,
        @Header("Authorization") auth:String?
    ): Call<Response>
}