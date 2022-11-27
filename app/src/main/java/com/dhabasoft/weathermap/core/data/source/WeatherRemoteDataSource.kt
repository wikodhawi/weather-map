package com.dhabasoft.weathermap.core.data.source

import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.core.data.source.remote.ApiService
import com.dhabasoft.weathermap.core.data.source.response.detailcity.DetailCity
import com.dhabasoft.weathermap.core.data.source.response.error.ErrorResponse
import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
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

    fun detailCity(cityId: String): Flow<ApiResponse<DetailCity>> {
        return flow {
            try {
                val response = apiService.detailCity(cityId)
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val responseError =
                    Gson().fromJson(e.response()?.errorBody()?.string(), ErrorResponse::class.java)
                emit(ApiResponse.Error(responseError.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun FindCity.mapResponseToCityEntity(): CityEntity {
        return CityEntity(
            id = this.id,
            cityName = this.name,
            countryCode = this.sys.country,
            weatherIcon = if (this.weather.isNotEmpty()) this.weather[0].icon else "",
            weatherDescription = if (this.weather.isNotEmpty()) this.weather[0].description else ""
        )
    }
}