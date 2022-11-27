package com.dhabasoft.weathermap.core.domain.repository

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface IWeatherRepository {
    fun findCity(city: String) : Flow<Resource<FindCity>>
}