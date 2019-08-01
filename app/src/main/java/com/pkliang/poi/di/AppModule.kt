package com.pkliang.poi.di

import android.app.Application
import android.content.Context
import com.pkliang.poi.core.SchedulerProvider
import com.pkliang.poi.domain.core.scheduler.Scheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideSchedule(): Scheduler = SchedulerProvider()
}
