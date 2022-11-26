package com.dhabasoft.androidintermediate.core

import androidx.paging.PagingData
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.StoriesRemoteDataSource
import com.dhabasoft.androidintermediate.core.data.source.remote.ApiResponse
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.dhabasoft.androidintermediate.core.domain.repository.IStoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class StoriesRepository @Inject constructor(
    private val remoteDataSource: StoriesRemoteDataSource
) : IStoriesRepository {
    override fun addStory(
        description: String,
        filePart: MultipartBody.Part,
        token: String,
        lat: String,
        lon: String
    ): Flow<Resource<GeneralResponse>> = flow {
        emit(Resource.Loading())
        when (val responseData =
            remoteDataSource.addStory(description, filePart, token, lat, lon).first()) {
            is ApiResponse.Success -> {
                emit(Resource.Success(responseData.data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(responseData.errorMessage))
            }
        }
    }

    override fun getStoriesPaging(token: String): Flow<PagingData<Story>> {
        return remoteDataSource.getStoriesPaging(token)
    }

    override fun getStoriesLocation(token: String): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading())
        when (val responseData = remoteDataSource.getStoriesLocation(token).first()) {
            is ApiResponse.Success -> {
                emit(Resource.Success(responseData.data.listStory))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(responseData.errorMessage))
            }
        }
    }
}