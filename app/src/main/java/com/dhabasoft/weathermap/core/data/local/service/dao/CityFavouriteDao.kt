package com.dhabasoft.weathermap.core.data.local.service.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dhabasoft.weathermap.core.data.local.CityEntity

/**
 * Created by dhaba
 */
@Dao
interface CityFavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteCity(cityFavouriteEntity: CityEntity)

    @Query("SELECT * FROM CityEntity WHERE id=:id")
    fun getById(id: Int): CityEntity?
}