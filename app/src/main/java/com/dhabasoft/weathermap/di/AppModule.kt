package com.dhabasoft.weathermap.di

import com.dhabasoft.weathermap.core.domain.usecase.LoginInteractor
import com.dhabasoft.weathermap.core.domain.usecase.RegisterInteractor
import com.dhabasoft.weathermap.core.domain.usecase.StoriesInteractor
import com.dhabasoft.weathermap.core.domain.usecase.WeatherInteractor
import com.dhabasoft.weathermap.view.login.LoginUseCase
import com.dhabasoft.weathermap.view.stories.StoriesUseCase
import com.dhabasoft.weathermap.view.register.RegisterUseCase
import com.dhabasoft.weathermap.view.weather.WeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideLoginUseCase(loginInteractor: LoginInteractor): LoginUseCase

    @Binds
    abstract fun provideStoriesUseCase(storiesInteractor: StoriesInteractor): StoriesUseCase

    @Binds
    abstract fun provideRegisterUseCase(registerInteractor: RegisterInteractor): RegisterUseCase

    @Binds
    abstract fun provideWeatherUseCase(weatherInteractor: WeatherInteractor): WeatherUseCase
}
