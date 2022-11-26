package com.dhabasoft.androidintermediate.core.domain.usecase

import com.dhabasoft.androidintermediate.core.RegisterRepository
import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.view.register.RegisterUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by dhaba
 */
class RegisterInteractor @Inject constructor(private val registerRepository: RegisterRepository) :
    RegisterUseCase {
   override fun register(
        email: String,
        name: String,
        password: String
    ): Flow<Resource<GeneralResponse>> {
        return  registerRepository.register(name, email, password)
    }
}