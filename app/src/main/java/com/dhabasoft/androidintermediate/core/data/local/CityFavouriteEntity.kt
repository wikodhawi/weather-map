package com.dhabasoft.androidintermediate.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class CityFavouriteEntity (
        @PrimaryKey
        val id: Int,
        val cityId: String,
        val cityName: String,
        val createdDate: Date
        )