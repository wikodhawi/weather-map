package com.dhabasoft.weathermap.core

import com.dhabasoft.weathermap.core.data.source.WeatherRemoteDataSource
import com.dhabasoft.weathermap.core.domain.repository.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource
) : IWeatherRepository {

}