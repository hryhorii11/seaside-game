package com.example.suicidergame.di

import com.example.suicidergame.data.repository.ScoreRepository
import com.example.suicidergame.data.repository.ScoreRepositoryImpl
import com.example.suicidergame.data.room.ScoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideScoreRepository(scoreDao: ScoreDao): ScoreRepository = ScoreRepositoryImpl(scoreDao)
}