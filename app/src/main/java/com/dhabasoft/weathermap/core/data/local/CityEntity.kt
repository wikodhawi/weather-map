package com.dhabasoft.weathermap.core.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
class CityEntity(
    @PrimaryKey
    val id: Int = 0,
    val cityName: String,
    val countryCode: String,
    val weatherIcon: String,
    val weatherDescription: String,
): Parcelable