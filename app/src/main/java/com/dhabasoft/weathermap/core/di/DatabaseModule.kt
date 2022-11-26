package com.dhabasoft.weathermap.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dhabasoft.weathermap.core.data.local.service.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "RecruitmentJuloDb.db").allowMainThreadQueries().fallbackToDestructiveMigration().addCallback(object: RoomDatabase.Callback() {
        }).build()

    @Provides
    fun provideCityFavouriteDao(appDatabase: AppDatabase) = appDatabase.cityFavouriteDao()
}