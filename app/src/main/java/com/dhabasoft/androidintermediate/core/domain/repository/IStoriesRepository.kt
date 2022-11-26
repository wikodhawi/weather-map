package com.dhabasoft.androidintermediate.core.domain.repository

import androidx.paging.PagingData
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

/**
 * Created by dhaba
 */
interface IStoriesRepository {
    fun getStoriesLocation(token: String): Flow<Resource<List<Story>>>
    fun addStory(
        description: String,
        filePart: MultipartBody.Part,
        token: String,
        lat: String,
        lon: String
    ): Flow<Resource<GeneralResponse>>
    fun getStoriesPaging(token: String): Flow<PagingData<Story>>
}