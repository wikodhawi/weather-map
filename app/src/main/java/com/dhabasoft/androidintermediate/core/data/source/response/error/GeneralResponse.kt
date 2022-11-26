package com.dhabasoft.androidintermediate.core.data.source.response.error


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GeneralResponse(
    @SerializedName("error")
    @Expose
    val error: Boolean,
    @SerializedName("message")
    @Expose
    val message: String
)