package com.dhabasoft.androidintermediate.view.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class StoriesViewModel @Inject constructor(private val storiesUseCase: StoriesUseCase) : ViewModel() {
    fun getStoriesPaging(token: String): LiveData<PagingData<Story>> =
        storiesUseCase.getStoriesPaging(token).asLiveData()
}