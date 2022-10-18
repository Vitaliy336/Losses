package com.example.losses.repository.remote

import com.example.losses.repository.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("statistics/latest")
    fun getLatest(): Call<ApiResponse>

    @GET("statistics/{date}")
    fun getStatsByDate(@Path("date") date: String): Call<ApiResponse>
}