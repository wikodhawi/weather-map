package com.dhabasoft.weathermap.ui.utils

import com.dhabasoft.weathermap.core.data.source.response.detailcity.DetailCity
import com.dhabasoft.weathermap.core.data.source.response.findcity.FindCity
import com.google.gson.Gson

/**
 * Created by dhaba
 */
object DataDummy {
    private fun getStringFromFileJson(path: String): String {
        val bufferedReader = javaClass.getResourceAsStream(path)
        return bufferedReader?.bufferedReader()?.use { it.readText() } ?: ""
    }

    val generateGetCity: FindCity =
        Gson().fromJson(getStringFromFileJson("/get_city.json"), FindCity::class.java)

    val generateGetDetailCity: DetailCity =
        Gson().fromJson(getStringFromFileJson("/get_details.json"), DetailCity::class.java)
}