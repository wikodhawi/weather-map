package com.dhabasoft.androidintermediate.view.login

import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.login.LoginResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface LoginUseCase {
    fun login(email: String, password: String): Flow<Resource<LoginResult>>
}