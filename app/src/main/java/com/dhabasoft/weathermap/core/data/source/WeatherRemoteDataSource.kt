package com.dhabasoft.weathermap.core.data.source

import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailCityEntity
import com.dhabasoft.weathermap.core.data.source.MapperEntity.mapResponseToCityEntity
import com.dhabasoft.weathermap.core.data.source.MapperEntity.mapToDetailCityEntity
import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.core.data.source.remote.ApiService
import com.dhabasoft.weathermap.core.data.source.response.error.ErrorResponse
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
class WeatherRemoteDataSource @Inject constructor(private val apiService: ApiService) {
    fun findCity(city: String): Flow<ApiResponse<List<CityEntity>>> {
        return flow {
            try {
                val response = apiService.findCity(city)
                val listCity = listOf(response.mapResponseToCityEntity())
                emit(ApiResponse.Success(listCity))
            } catch (e: HttpException) {
                val responseError =
                    Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
                emit(ApiResponse.Error(responseError.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun detailCity(cityId: String): Flow<ApiResponse<List<DetailCityEntity>>> {
        return flow {
            try {
                val response = apiService.detailCity(cityId)
                val detailCities = response.mapToDetailCityEntity()
                emit(ApiResponse.Success(detailCities))
            } catch (e: HttpException) {
                val responseError =
                    Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
                emit(ApiResponse.Error(responseError.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}