package com.dhabasoft.weathermap.core.domain.usecase

import com.dhabasoft.weathermap.core.WeatherRepository
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.source.response.detailcity.DetailCity
import com.dhabasoft.weathermap.view.weather.WeatherUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by dhaba
 */
class WeatherInteractor @Inject constructor(private val weatherRepository: WeatherRepository) :
    WeatherUseCase {
    override fun findCity(city: String): Flow<Resource<List<CityEntity>>> {
        return  weatherRepository.findCity(city)
    }

    override fun detailCity(cityId: String): Flow<Resource<DetailCity>> {
        return weatherRepository.detailCity(cityId)
    }

    override fun setOrRemoveFromCityFavorite(cityEntity: CityEntity) {
        weatherRepository.setOrRemoveFromMovieFavourite(cityEntity)
    }

    override fun getIsFavoriteCity(cityId: Int) {
        weatherRepository.getIsFavorite(cityId)
    }

    override fun getFlowIsFavourite(): Flow<Boolean> {
        return weatherRepository.getFlowIsFavourite()
    }
}