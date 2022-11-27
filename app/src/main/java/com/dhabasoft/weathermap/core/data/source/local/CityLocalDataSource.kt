package com.dhabasoft.weathermap.core.data.source.local

import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.service.dao.CityFavouriteDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityLocalDataSource @Inject constructor(private val cityFavouriteDao: CityFavouriteDao
) {

    fun setOrRemoveFromMovieFavourite(cityEntity: CityEntity) {
        cityFavouriteDao.insertFavouriteCity(cityEntity)
    }
}