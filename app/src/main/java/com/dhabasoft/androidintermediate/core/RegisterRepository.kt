package com.dhabasoft.androidintermediate.core

import com.dhabasoft.androidintermediate.core.data.Resource
import com.dhabasoft.androidintermediate.core.data.source.RegisterRemoteDataSource
import com.dhabasoft.androidintermediate.core.data.source.remote.ApiResponse
import com.dhabasoft.androidintermediate.core.data.source.response.error.GeneralResponse
import com.dhabasoft.androidintermediate.core.domain.repository.IRegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class RegisterRepository @Inject constructor(
    private val remoteDataSource: RegisterRemoteDataSource
) : IRegisterRepository {
    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<GeneralResponse>> = flow{
        emit(Resource.Loading())
        when (val responseData = remoteDataSource.register(email, name, password).first()) {
            is ApiResponse.Success -> {
                emit(Resource.Success(responseData.data))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(responseData.errorMessage))
            }
        }
    }
}