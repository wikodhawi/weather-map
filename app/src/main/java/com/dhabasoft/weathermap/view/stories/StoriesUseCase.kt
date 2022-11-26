package com.dhabasoft.weathermap.view.stories

import androidx.paging.PagingData
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
import com.dhabasoft.weathermap.core.data.source.response.stories.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

/**
 * Created by dhaba
 */
interface StoriesUseCase {
    fun getStoriesPaging(token: String): Flow<PagingData<Story>>
    fun getStoriesLocation(token: String): Flow<Resource<List<Story>>>
    fun addStory(
        description: String,
        filePart: MultipartBody.Part,
        token: String,
        lat: String,
        lon: String
    ): Flow<Resource<GeneralResponse>>
}