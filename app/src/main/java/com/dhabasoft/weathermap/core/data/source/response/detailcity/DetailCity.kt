package com.dhabasoft.weathermap.core.data.source.response.detailcity


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DetailCity(
    @SerializedName("city")
    @Expose
    val city: City,
    @SerializedName("cnt")
    @Expose
    val cnt: Int,
    @SerializedName("cod")
    @Expose
    val cod: String,
    @SerializedName("list")
    @Expose
    val list: List<ListData>,
    @SerializedName("message")
    @Expose
    val message: Int
)