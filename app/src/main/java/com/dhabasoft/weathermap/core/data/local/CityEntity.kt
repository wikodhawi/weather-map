package com.dhabasoft.weathermap.core.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
class CityEntity(
    @PrimaryKey
    val id: String,
    val cityName: String,
    val countryCode: String,
    val weatherIcon: String,
    val weatherDescription: String,
): Parcelable