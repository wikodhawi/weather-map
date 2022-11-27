package com.dhabasoft.weathermap.core.data.source

import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailByHourEntity
import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.core.data.source.remote.ApiService
import com.dhabasoft.weathermap.core.data.source.response.detailcity.DetailCity
import com.dhabasoft.weathermap.core.data.source.response.error.ErrorResponse
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
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
                val x = response.mapToDetailCityEntity()
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

    private fun DetailCity.mapToDetailCityEntity(): Map<String, MutableList<DetailByHourEntity>> {
        val today = Calendar.getInstance()
        today.time = Date()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val msPerDay = 60*60*24*1000
        val mapDetailHour = mutableMapOf<String, MutableList<DetailByHourEntity>>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateFormatSave = SimpleDateFormat("yyyy-MM-dd")
        val timeFormatSave = SimpleDateFormat("HH:mm:ss")
        for (i in this.list) {
            val dateList = Calendar.getInstance()
            try {
                val dateParse = dateFormat.parse(i.dtTxt)
                dateList.time = dateParse
                dateList.set(Calendar.HOUR_OF_DAY, 0)
                dateList.set(Calendar.MINUTE, 0)
                dateList.set(Calendar.SECOND, 0)
                dateList.set(Calendar.MILLISECOND, 0)

                val dateDiff = (dateList.timeInMillis - today.timeInMillis) / msPerDay
                if (dateDiff > 2) {
                    break
                }

                val listDetailHour = mapDetailHour[dateFormatSave.format(dateList.time)]
                if (listDetailHour == null) {
                    mapDetailHour[dateFormatSave.format(dateList.time)] = mutableListOf()
                }
                mapDetailHour[dateFormatSave.format(dateList.time)]?.add(DetailByHourEntity(
                            temperature = i.main.temp.toString(),
                            humidity = i.main.humidity.toString(),
                            windCondition = i.wind.speed.toString(),
                            time = timeFormatSave.format(dateParse.time)
                        ))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return mapDetailHour
    }
}