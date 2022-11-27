package com.dhabasoft.weathermap.core.data.source.local

import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.service.dao.CityFavouriteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityLocalDataSource @Inject constructor(private val cityFavouriteDao: CityFavouriteDao
) {
    fun getIsFavouriteCity(cityId: Int) : Flow<Boolean> = flow {
        val cityFavorite = cityFavouriteDao.getById(cityId)
        emit(cityFavorite != null)
    }

    fun setFavoriteCity(cityEntity: CityEntity) {
        cityFavouriteDao.insertFavouriteCity(cityEntity)
    }
}