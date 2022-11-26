package com.dhabasoft.androidintermediate.view.register

import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface RegisterUseCase {
    fun register(
        email: String,
        name: String,
        password: String
    ): Flow<Resource<GeneralResponse>>
}