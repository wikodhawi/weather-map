package com.dhabasoft.weathermap.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dhabasoft.weathermap.core.data.source.remote.ApiService
import com.dhabasoft.weathermap.core.data.source.response.stories.Story
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class StoriesRemotePagingDataSource @Inject constructor(private val apiService: ApiService) :
    PagingSource<Int, Story>() {
    var authorization: String = ""

    override fun getRefreshKey(state: PagingState<Int, Story>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val position = params.key ?: 1

        return try {
//            EspressoIdlingResource.increment()
            apiService.getStoriesPaging(
                authorization = authorization,
                page = position
            ).run {
                val data = this

                if (data.listStory.isEmpty()) {
                    LoadResult.Error(Exception("All Loader"))
                } else {
                    LoadResult.Page(
                        data = data.listStory,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (this.listStory.isEmpty()) null else position + 1
                    )
                }
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}