package com.dhabasoft.weathermap.view.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase): ViewModel() {
    fun findCity(city: String): LiveData<Resource<List<CityEntity>>> =
        weatherUseCase.findCity(city).asLiveData()
}