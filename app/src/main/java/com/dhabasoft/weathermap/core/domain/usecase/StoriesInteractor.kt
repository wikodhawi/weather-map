package com.dhabasoft.weathermap.core.domain.usecase

import androidx.paging.PagingData
import com.dhabasoft.weathermap.core.StoriesRepository
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
import com.dhabasoft.weathermap.core.data.source.response.stories.Story
import com.dhabasoft.weathermap.view.stories.StoriesUseCase
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