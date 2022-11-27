package com.dhabasoft.weathermap.core.domain.usecase

import com.dhabasoft.weathermap.core.WeatherRepository
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import com.dhabasoft.weathermap.view.weather.WeatherUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by dhaba
 */
class WeatherInteractor @Inject constructor(private val weatherRepository: WeatherRepository) :
    WeatherUseCase {
    override fun findCity(city: String): Flow<Resource<FindCity>> {
        return  weatherRepository.findCity(city)
    }
}