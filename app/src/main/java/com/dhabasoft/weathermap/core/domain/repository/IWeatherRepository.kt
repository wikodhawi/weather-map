package com.dhabasoft.weathermap.core.domain.repository

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailCityEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface IWeatherRepository {
    fun findCity(city: String) : Flow<Resource<List<CityEntity>>>
    fun detailCity(cityId: String) : Flow<Resource<List<DetailCityEntity>>>
    fun getIsFavorite(cityId: Int)
    fun setOrRemoveFromMovieFavourite(cityEntity: CityEntity)
    fun getFlowIsFavourite() : Flow<Boolean>
}