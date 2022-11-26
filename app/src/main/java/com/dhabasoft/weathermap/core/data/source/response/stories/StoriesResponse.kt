package com.dhabasoft.weathermap.core.data.source.response.stories


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class StoriesResponse(
    @SerializedName("error")
    @Expose
    val error: Boolean,
    @SerializedName("listStory")
    @Expose
    val listStory: List<Story>,
    @SerializedName("message")
    @Expose
    val message: String
)