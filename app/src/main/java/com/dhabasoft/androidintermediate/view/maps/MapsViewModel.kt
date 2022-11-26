package com.dhabasoft.androidintermediate.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.stories.Story
import com.dhabasoft.androidintermediate.view.stories.StoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class MapsViewModel @Inject constructor(private val storiesUseCase: StoriesUseCase) : ViewModel() {
    fun getStoriesLocation(token: String): LiveData<Resource<List<Story>>> =
        storiesUseCase.getStoriesLocation(token).asLiveData()
}