package com.dhabasoft.weathermap.core.data.source.local

import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.service.dao.CityFavouriteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityLocalDataSource @Inject constructor(
    private val cityFavouriteDao: CityFavouriteDao
) {
    val flowIsFavourite = MutableStateFlow(false)

    fun getIsFavouriteCity(cityId: Int) {
        val cityFavorite = cityFavouriteDao.getById(cityId)
        flowIsFavourite.value = cityFavorite != null
    }

    fun setOrRemoveCityFavorite(cityEntity: CityEntity) {
        val cityFavorite = cityFavouriteDao.getById(cityEntity.id)
        if (cityFavorite == null)
            cityFavouriteDao.insertFavouriteCity(cityEntity)
        else
            cityFavouriteDao.delete(cityEntity.id)
        getIsFavouriteCity(cityEntity.id)
    }

    fun getCities(cityName: String) : Flow<List<CityEntity>> = flow {
         emit(cityFavouriteDao.getByCityName(cityName))
    }
}