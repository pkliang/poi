package com.pkliang.poi.di

import com.pkliang.poi.MapsActivity
import com.pkliang.poi.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinder {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMapsActivity(): MapsActivity
}
