package com.dhabasoft.weathermap.core.data.source.remote

import com.dhabasoft.weathermap.BuildConfig
import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import com.dhabasoft.weathermap.core.data.source.response.login.LoginResponse
import com.dhabasoft.weathermap.core.data.source.response.stories.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStoriesPaging(@Header("Authorization") authorization: String, @Query("page") page: Int): StoriesResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): GeneralResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part filePart: MultipartBody.Part ,
        @Part("description") description: String,
        @Header("Authorization") authorization: String,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float,
    ): GeneralResponse

    @GET("stories?location=1")
    suspend fun getStoriesLocation(@Header("Authorization") authorization: String): StoriesResponse

    @GET("data/2.5/weather?units=metric&appid=${BuildConfig.API_KEY}")
    suspend fun findCity(@Query("q") city: String): FindCity
}
