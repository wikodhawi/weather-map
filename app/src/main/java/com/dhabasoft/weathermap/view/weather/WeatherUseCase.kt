package com.dhabasoft.weathermap.view.weather

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.source.response.detailcity.DetailCity
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface WeatherUseCase {
    fun findCity(
        city: String
    ): Flow<Resource<List<CityEntity>>>

    fun detailCity(
        cityId: String
    ): Flow<Resource<DetailCity>>

    fun setOrRemoveFromCityFavorite(cityEntity: CityEntity)
    fun getIsFavoriteCity(cityId: Int)
    fun getFlowIsFavourite() : Flow<Boolean>
}