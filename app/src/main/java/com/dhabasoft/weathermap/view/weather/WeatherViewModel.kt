package com.dhabasoft.weathermap.view.weather

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase): ViewModel() {
}