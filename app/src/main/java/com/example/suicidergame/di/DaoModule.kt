package com.example.suicidergame.di

import android.app.Application
import com.example.suicidergame.data.room.AppDataBase
import com.example.suicidergame.data.room.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Provides
    @Singleton
    fun provideDataBase(app: Application): AppDataBase {
        return AppDataBase.getInstance(app)
    }


    @Provides
    @Singleton
    fun providePasswordDao(db: AppDataBase): ScoreDao = db.getScoreDao()
}