package com.dhabasoft.weathermap.core.data.source

import com.dhabasoft.weathermap.core.data.source.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class WeatherRemoteDataSource @Inject constructor(private val apiService: ApiService) {

}