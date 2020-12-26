package com.example.busticketactivity.listener

import com.example.busticketactivity.firebase.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetStatus {
    @GET("{id}/status")

    fun getTransaksi(
        @Header("Authorization") auth: String?,
        @Path("id") otp:String?

    ): Call<Response>
}