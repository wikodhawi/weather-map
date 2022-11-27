package com.dhabasoft.weathermap.core

import com.dhabasoft.weathermap.core.data.Resource
import com.dhabasoft.weathermap.core.data.source.LoginRemoteDataSource
import com.dhabasoft.weathermap.core.data.source.local.CityLocalDataSource
import com.dhabasoft.weathermap.core.data.source.remote.ApiResponse
import com.dhabasoft.weathermap.core.data.source.response.login.LoginResult
import com.dhabasoft.weathermap.core.domain.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by dhaba
 */
@Singleton
class LoginRepository @Inject constructor(
    private val remoteDataSource: LoginRemoteDataSource,
    private val cityLocalDataSource: CityLocalDataSource
) : ILoginRepository {
    override fun login(email: String, password: String): Flow<Resource<LoginResult>> = flow {
//        cityLocalDataSource.setOrRemoveFromMovieFavourite(CityFavouriteEntity(1, "CityId", "DhabaCity", "ID"))
        emit(Resource.Loading())
        when(val responseData = remoteDataSource.login(email, password).first())
        {
            is ApiResponse.Success -> {
                emit(Resource.Success(responseData.data.loginResult))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(responseData.errorMessage))
            }
        }
    }
}