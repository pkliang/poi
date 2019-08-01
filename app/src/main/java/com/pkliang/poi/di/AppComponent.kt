package com.pkliang.poi.di

import android.app.Application
import com.pkliang.poi.App
import com.pkliang.poi.di.network.NetworkModule
import com.pkliang.poi.di.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        ActivityBinder::class,
        NetworkModule::class,
        MapModule::class
    ]
)

interface AppComponent : AndroidInjector<App> {

    override fun inject(app: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
