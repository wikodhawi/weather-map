package com.dhabasoft.weathermap.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.stories.Story
import com.dhabasoft.weathermap.view.stories.StoriesUseCase
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