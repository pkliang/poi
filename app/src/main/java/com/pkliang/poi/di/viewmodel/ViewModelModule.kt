package com.pkliang.poi.di.viewmodel

import androidx.lifecycle.ViewModel
import com.pkliang.poi.MapViewModel
import com.pkliang.poi.di.scope.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ViewModelFactoryModule::class])
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: MapViewModel): ViewModel
}
