package com.dhabasoft.weathermap.core.data.local.detailcity

import java.util.*

/**
 * Created by dhaba
 */
data class DetailCityEntity (val dateString: Calendar, val detailByHour : List<DetailByHourEntity>) {
}