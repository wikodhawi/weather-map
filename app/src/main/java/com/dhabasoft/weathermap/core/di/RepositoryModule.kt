package com.dhabasoft.weathermap.core.di

import com.dhabasoft.weathermap.core.LoginRepository
import com.dhabasoft.weathermap.core.RegisterRepository
import com.dhabasoft.weathermap.core.StoriesRepository
import com.dhabasoft.weathermap.core.domain.repository.ILoginRepository
import com.dhabasoft.weathermap.core.domain.repository.IRegisterRepository
import com.dhabasoft.weathermap.core.domain.repository.IStoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideLoginRepository(loginRepository: LoginRepository): ILoginRepository

    @Binds
    abstract fun provideStoriesRepository(storiesRepository: StoriesRepository): IStoriesRepository

    @Binds
    abstract fun provideRegisterRepository(registerRepository: RegisterRepository): IRegisterRepository
}