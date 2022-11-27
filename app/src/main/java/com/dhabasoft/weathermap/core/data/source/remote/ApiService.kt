package com.dhabasoft.weathermap.core.data.source.remote

import com.dhabasoft.weathermap.BuildConfig
import com.dhabasoft.weathermap.core.data.source.response.detailcity.DetailCity
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("data/2.5/weather?units=metric&appid=${BuildConfig.API_KEY}")
    suspend fun findCity(@Query("q") city: String): FindCity

    @GET("data/2.5/forecast?units=metric&appid=${BuildConfig.API_KEY}")
    suspend fun detailCity(@Query("id") cityId: String): DetailCity
}
