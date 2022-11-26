package com.dhabasoft.androidintermediate.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {
    fun register(email: String, name: String, password: String): LiveData<Resource<GeneralResponse>> =
        registerUseCase.register(email, name, password).asLiveData()
}