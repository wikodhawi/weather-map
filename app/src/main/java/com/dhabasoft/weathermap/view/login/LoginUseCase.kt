package com.dhabasoft.weathermap.view.login

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.login.LoginResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface LoginUseCase {
    fun login(email: String, password: String): Flow<Resource<LoginResult>>
}