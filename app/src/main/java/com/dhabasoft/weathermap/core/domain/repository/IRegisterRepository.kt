package com.dhabasoft.weathermap.core.domain.repository

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.response.error.GeneralResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by dhaba
 */
interface IRegisterRepository {
    fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<GeneralResponse>>
}