package com.dhabasoft.androidintermediate.core.domain.usecase

import com.dhabasoft.androidintermediate.core.LoginRepository
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.login.LoginResult
import com.dhabasoft.androidintermediate.view.login.LoginUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by dhaba
 */
class LoginInteractor @Inject constructor(private val loginRepository: LoginRepository) : LoginUseCase {
    override fun login(email: String, password: String): Flow<Resource<LoginResult>> {
        return loginRepository.login(email, password)
    }
}