package com.dhabasoft.androidintermediate.core.domain.usecase

import androidx.paging.PagingData
import com.dhabasoft.androidintermediate.core.StoriesRepository
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.dhabasoft.androidintermediate.view.stories.StoriesUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by dhaba
 */
class StoriesInteractor @Inject constructor(private val storiesRepository: StoriesRepository) :
    StoriesUseCase {

    override fun addStory(
        description: String,
        filePart: MultipartBody.Part,
        token: String,
        lat: String,
        lon: String
    ): Flow<Resource<GeneralResponse>> {
        return storiesRepository.addStory(description, filePart, token, lat, lon)
    }

    override fun getStoriesPaging(token: String): Flow<PagingData<Story>> {
        return storiesRepository.getStoriesPaging(token)
    }

    override fun getStoriesLocation(token: String): Flow<Resource<List<Story>>> {
        return storiesRepository.getStoriesLocation(token)
    }
}