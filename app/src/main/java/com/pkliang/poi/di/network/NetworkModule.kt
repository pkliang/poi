package com.pkliang.poi.di.network

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pkliang.poi.BuildConfig.DEBUG
import com.pkliang.poi.data.network.NetworkConstants.Api.API_URL
import com.pkliang.poi.data.network.NetworkConstants.HttpRequest.HTTP_REQUEST_TIMEOUT_SECONDS
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(
            Json.nonstrict
                .asConverterFactory(MediaType.get("application/json; charset=utf-8"))
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(application: Application): OkHttpClient.Builder = OkHttpClient.Builder()
        .callTimeout(HTTP_REQUEST_TIMEOUT_SECONDS, SECONDS)
        .apply {
            if (DEBUG) addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addInterceptor(ChuckInterceptor(application))
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient = okHttpClientBuilder.build()

    @Provides
    @Singleton
    fun provideRetrofit(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): Retrofit =
        retrofitBuilder.baseUrl(API_URL).client(okHttpClient).build()
}
