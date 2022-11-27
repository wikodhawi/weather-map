package com.dhabasoft.weathermap.core.data.source

import com.dhabasoft.weathermap.core.data.local.CityEntity
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailByHourEntity
import com.dhabasoft.weathermap.core.data.local.detailcity.DetailCityEntity
import com.dhabasoft.weathermap.core.data.source.response.detailcity.DetailCity
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dhaba
 */
object MapperEntity {
    fun FindCity.mapResponseToCityEntity(): CityEntity {
        return CityEntity(
            id = this.id,
            cityName = this.name,
            countryCode = this.sys.country,
            weatherIcon = if (this.weather.isNotEmpty()) this.weather[0].icon else "",
            weatherDescription = if (this.weather.isNotEmpty()) this.weather[0].description else ""
        )
    }

    fun DetailCity.mapToDetailCityEntity(): List<DetailCityEntity> {
        val result = mutableListOf<DetailCityEntity>()
        val today = Calendar.getInstance()
        today.time = Date()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        val msPerDay = 60*60*24*1000
        val mapDetailHour = mutableMapOf<String, MutableList<DetailByHourEntity>>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateFormatSave = SimpleDateFormat("yyyy-MM-dd")
        val timeFormatSave = SimpleDateFormat("HH:mm:ss")
        for (i in this.list) {
            val dateList = Calendar.getInstance()
            try {
                val dateParse = dateFormat.parse(i.dtTxt)
                dateList.time = dateParse
                dateList.set(Calendar.HOUR_OF_DAY, 0)
                dateList.set(Calendar.MINUTE, 0)
                dateList.set(Calendar.SECOND, 0)
                dateList.set(Calendar.MILLISECOND, 0)

                val dateDiff = (dateList.timeInMillis - today.timeInMillis) / msPerDay
                if (dateDiff > 2) {
                    break
                }

                val listDetailHour = mapDetailHour[dateFormatSave.format(dateList.time)]
                if (listDetailHour == null) {
                    mapDetailHour[dateFormatSave.format(dateList.time)] = mutableListOf()
                }
                mapDetailHour[dateFormatSave.format(dateList.time)]?.add(
                    DetailByHourEntity(
                            temperature = i.main.temp.toString(),
                            humidity = i.main.humidity.toString(),
                            windCondition = i.wind.speed.toString(),
                            time = timeFormatSave.format(dateParse.time)
                        )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        for (i in mapDetailHour) {
            result.add(DetailCityEntity(i.key, i.value))
        }
        return result
    }
}