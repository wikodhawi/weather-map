package com.dhabasoft.weathermap.di

import com.dhabasoft.weathermap.core.domain.usecase.WeatherInteractor
import com.dhabasoft.weathermap.view.weather.WeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideWeatherUseCase(weatherInteractor: WeatherInteractor): WeatherUseCase
}
