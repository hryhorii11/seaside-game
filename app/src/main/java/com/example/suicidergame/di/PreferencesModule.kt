package com.example.suicidergame.di

import android.app.Application
import com.example.suicidergame.data.preferences.PreferencesManager
import com.example.suicidergame.data.preferences.PreferencesManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {

    @Provides
    @Singleton
    fun providePreferencesManager(context: Application): PreferencesManager =
        PreferencesManagerImpl(context)
}