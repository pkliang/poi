package com.pkliang.poi.di

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.pkliang.poi.data.network.MediaWikiRetrofitService
import com.pkliang.poi.data.repository.ArticleRepositoryImpl
import com.pkliang.poi.data.repository.GeolocationRepositoryImpl
import com.pkliang.poi.domain.nearby.repository.ArticleRepository
import com.pkliang.poi.domain.nearby.repository.GeolocationRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class MapModule {

    @Singleton
    @Provides
    fun provideMediaWikiRetrofitService(retrofit: Retrofit): MediaWikiRetrofitService =
        retrofit.create(MediaWikiRetrofitService::class.java)

    @Singleton
    @Provides
    fun provideArticleRepository(articleRepository: ArticleRepositoryImpl): ArticleRepository = articleRepository

    @Singleton
    @Provides
    fun provideGeolocationRepository(geolocationRepository: GeolocationRepositoryImpl): GeolocationRepository =
        geolocationRepository

    @Singleton
    @Provides
    fun provideRxLocation(context: Context) = RxLocation(context).apply {
        setDefaultTimeout(LOCATION_REQUEST_TIME_OUT, TimeUnit.SECONDS)
    }

    @Singleton
    @Provides
    fun provideLocationRequest(): LocationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        .setInterval(LOCATION_REQUEST_INTERVAL)
        .setSmallestDisplacement(SMALLEST_DISPLACEMENT)

    companion object {
        private const val LOCATION_REQUEST_TIME_OUT = 60L
        private const val LOCATION_REQUEST_INTERVAL = 10 * 1000L
        private const val SMALLEST_DISPLACEMENT = 50f
    }
}
