package com.dhabasoft.weathermap.core.data.source.response.login


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginResponse(
    @SerializedName("error")
    @Expose
    val error: Boolean,
    @SerializedName("loginResult")
    @Expose
    val loginResult: LoginResult,
    @SerializedName("message")
    @Expose
    val message: String
)