package com.dhabasoft.androidintermediate.core.data.source.response.stories


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    @SerializedName("createdAt")
    @Expose
    val createdAt: String ?= "",
    @SerializedName("description")
    @Expose
    val description: String ?= "",
    @SerializedName("id")
    @Expose
    val id: String ?= "",
    @SerializedName("lat")
    @Expose
    val lat: String ?= "",
    @SerializedName("lon")
    @Expose
    val lon: String ?= "",
    @SerializedName("name")
    @Expose
    val name: String ?= "",
    @SerializedName("photoUrl")
    @Expose
    val photoUrl: String ?= ""
): Parcelable