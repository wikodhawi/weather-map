package com.dhabasoft.androidintermediate.core.di

import com.dhabasoft.androidintermediate.core.LoginRepository
import com.dhabasoft.androidintermediate.core.RegisterRepository
import com.dhabasoft.androidintermediate.core.StoriesRepository
import com.dhabasoft.androidintermediate.core.domain.repository.ILoginRepository
import com.dhabasoft.androidintermediate.core.domain.repository.IRegisterRepository
import com.dhabasoft.androidintermediate.core.domain.repository.IStoriesRepository
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