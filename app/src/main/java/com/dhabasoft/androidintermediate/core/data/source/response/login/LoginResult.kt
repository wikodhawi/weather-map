package com.dhabasoft.androidintermediate.core.data.source.response.login


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginResult(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("userId")
    @Expose
    val userId: String
)