package com.dhabasoft.weathermap.core.data.source

import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.core.data.source.remote.ApiService
import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class RegisterRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    fun register(email: String, name: String, password: String): Flow<ApiResponse<GeneralResponse>> {
        return flow {
            try {
                val response = apiService.register(name, email, password)
                emit(ApiResponse.Success(response))
            } catch (e : HttpException){
                val responseError = Gson().fromJson(e.response()?.errorBody()?.string(), GeneralResponse::class.java)
                emit(ApiResponse.Error(responseError.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}