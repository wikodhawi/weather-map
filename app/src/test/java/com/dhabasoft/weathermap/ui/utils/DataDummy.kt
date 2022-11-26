package com.dhabasoft.weathermap.ui.utils

import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
import com.dhabasoft.weathermap.core.data.source.response.login.LoginResponse
import com.dhabasoft.weathermap.core.data.source.response.stories.StoriesResponse
import com.google.gson.Gson

/**
 * Created by dhaba
 */
object DataDummy {
    private fun getStringFromFileJson(path: String): String {
        val bufferedReader = javaClass.getResourceAsStream(path)
        return bufferedReader?.bufferedReader()?.use { it.readText() } ?: ""
    }

    val generateLoginResponse: LoginResponse =
        Gson().fromJson(getStringFromFileJson("/login.json"), LoginResponse::class.java)

    val generateStoriesResponse: StoriesResponse =
        Gson().fromJson(getStringFromFileJson("/get_all_story.json"), StoriesResponse::class.java)

    val generateRegisterResponse: GeneralResponse =
        Gson().fromJson(getStringFromFileJson("/register.json"), GeneralResponse::class.java)

    val generateAddStoryResponse: GeneralResponse =
        Gson().fromJson(getStringFromFileJson("/add_story.json"), GeneralResponse::class.java)
}