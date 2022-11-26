package com.dhabasoft.androidintermediate.di

import com.dhabasoft.androidintermediate.core.domain.usecase.LoginInteractor
import com.dhabasoft.androidintermediate.core.domain.usecase.RegisterInteractor
import com.dhabasoft.androidintermediate.core.domain.usecase.StoriesInteractor
import com.dhabasoft.androidintermediate.view.login.LoginUseCase
import com.dhabasoft.androidintermediate.view.stories.StoriesUseCase
import com.dhabasoft.androidintermediate.view.register.RegisterUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun provideLoginUseCase(loginInteractor: LoginInteractor): LoginUseCase

    @Binds
    abstract fun provideStoriesUseCase(storiesInteractor: StoriesInteractor): StoriesUseCase

    @Binds
    abstract fun provideRegisterUseCase(registerInteractor: RegisterInteractor): RegisterUseCase
}
