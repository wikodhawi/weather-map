package com.dhabasoft.weathermap.view.register

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
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