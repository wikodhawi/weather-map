package com.dhabasoft.weathermap.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.login.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    fun login(email: String, password: String): LiveData<Resource<LoginResult>> =
        loginUseCase.login(email, password).asLiveData()
}