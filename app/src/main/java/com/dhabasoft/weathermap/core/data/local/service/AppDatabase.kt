package com.dhabasoft.weathermap.core.data.local.service

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dhabasoft.weathermap.core.data.local.CityFavouriteEntity
import com.dhabasoft.weathermap.core.data.local.service.dao.CityFavouriteDao


@Database(
    entities =[CityFavouriteEntity::class], version = 1, exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityFavouriteDao(): CityFavouriteDao
}