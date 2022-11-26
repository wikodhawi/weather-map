package com.dhabasoft.androidintermediate.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.view.stories.StoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class AddStoryViewModel @Inject constructor(private val storiesUseCase: StoriesUseCase) : ViewModel() {
    fun addStory(description: String, filePart: MultipartBody.Part, token: String, lat: String, lon: String): LiveData<Resource<GeneralResponse>> =
        storiesUseCase.addStory(description, filePart, token, lat, lon).asLiveData()
}