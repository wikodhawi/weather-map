package com.dhabasoft.weathermap.core.data.source.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dhabasoft.weathermap.core.data.local.CityFavouriteEntity
import com.dhabasoft.weathermap.core.data.local.service.dao.CityFavouriteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityLocalDataSource @Inject constructor(private val cityFavouriteDao: CityFavouriteDao
) {

    fun setOrRemoveFromMovieFavourite(cityFavouriteEntity: CityFavouriteEntity) {
        cityFavouriteDao.insertFavouriteCity(cityFavouriteEntity)
    }
}