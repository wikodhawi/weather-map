package com.dhabasoft.weathermap.view.weather

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface WeatherUseCase {
    fun findCity(
        city: String
    ): Flow<Resource<List<CityEntity>>>
}