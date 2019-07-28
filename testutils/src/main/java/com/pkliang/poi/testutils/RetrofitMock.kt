package com.pkliang.poi.testutils

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitMock {

    inline fun <reified T : Any> create(url: String): T = getRetrofit(url).create(T::class.java)

    fun getRetrofit(url: String): Retrofit =
        getRetrofitBuilder(url).client(getOkHttpBuilder().build()).build()

    private fun getRetrofitBuilder(url: String): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(
            Json.nonstrict
                .asConverterFactory(MediaType.get("application/json; charset=utf-8"))
        )
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private fun getOkHttpBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(DefaultInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
}
