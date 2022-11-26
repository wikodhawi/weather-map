package com.dhabasoft.androidintermediate.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dhabasoft.androidintermediate.core.data.source.remote.ApiResponse
import com.dhabasoft.androidintermediate.core.data.source.remote.ApiService
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.core.data.source.response.stories.StoriesResponse
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class StoriesRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val storiesRemotePagingDataSource: StoriesRemotePagingDataSource
) {
    fun getStoriesPaging(authorization: String): Flow<PagingData<Story>> {
        storiesRemotePagingDataSource.authorization = authorization
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 2,
                initialLoadSize = 8),
            pagingSourceFactory = { storiesRemotePagingDataSource }
        ).flow
    }

    fun addStory(
        description: String,
        filePart: MultipartBody.Part,
        token: String,
        lat: String,
        lon: String,
    ): Flow<ApiResponse<GeneralResponse>> {
        return flow {
            try {
                val response = apiService.addStory(filePart, description, token, lat.toFloat(), lon.toFloat())
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val responseError = Gson().fromJson(
                    e.response()?.errorBody()?.string(),
                    GeneralResponse::class.java
                )
                emit(ApiResponse.Error(responseError.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getStoriesLocation(token: String): Flow<ApiResponse<StoriesResponse>> {
        return flow {
            try {
                val response = apiService.getStoriesLocation(token)
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val responseError = Gson().fromJson(
                    e.response()?.errorBody()?.string(),
                    GeneralResponse::class.java
                )
                emit(ApiResponse.Error(responseError.message))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}