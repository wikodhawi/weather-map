package com.dhabasoft.weathermap.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class CityEntity(
    @PrimaryKey
    val id: String,
    val cityName: String,
    val countryCode: String,
    val weatherIcon: String,
    val weatherDescription: String,
)