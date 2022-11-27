package com.dhabasoft.weathermap.core.data.source.response.error


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ErrorResponse(
    @SerializedName("cod")
    @Expose
    val cod: Int,
    @SerializedName("message")
    @Expose
    val message: String
)