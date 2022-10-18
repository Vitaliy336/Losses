package com.example.losses.repository.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Data,
) {
    data class Data(
        @SerializedName("date") val date: String,
        @SerializedName("day") val day: String,
        @SerializedName("resource") val resource: String,
        @SerializedName("stats") val stats: HashMap<String, Int>,
        @SerializedName("increase") val increase: HashMap<String, Int>?,
    )
}