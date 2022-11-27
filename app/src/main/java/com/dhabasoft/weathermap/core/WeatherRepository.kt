package com.dhabasoft.weathermap.core

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.WeatherRemoteDataSource
import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import com.dhabasoft.weathermap.core.domain.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : IWeatherRepository {
    override fun findCity(city: String): Flow<Resource<FindCity>> = flow {
        emit(Resource.Loading())
        when (val responseData = remoteDataSource.findCity(city).first()) {
            is ApiResponse.Success -> {
                emit(Resource.Success(responseData.data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(responseData.errorMessage))
            }
        }
    }
}