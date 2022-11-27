package com.dhabasoft.weathermap.core.domain.usecase

import com.dhabasoft.weathermap.core.WeatherRepository
import com.dhabasoft.weathermap.view.weather.WeatherUseCase
import javax.inject.Inject

/**
 * Created by dhaba
 */
class WeatherInteractor @Inject constructor(private val weatherRepository: WeatherRepository) :
    WeatherUseCase {

}