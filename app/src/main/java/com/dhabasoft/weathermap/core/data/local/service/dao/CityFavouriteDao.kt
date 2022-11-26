package com.dhabasoft.weathermap.core.data.local.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.dhabasoft.weathermap.core.data.local.CityFavouriteEntity

/**
 * Created by dhaba
 */
@Dao
interface CityFavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteCity(cityFavouriteEntity: CityFavouriteEntity)
}