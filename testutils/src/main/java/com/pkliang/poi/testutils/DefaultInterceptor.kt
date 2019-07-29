package com.pkliang.poi.testutils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class DefaultInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header("Content-Type", "application/json")
            .method(original.method(), original.body())
        return chain.proceed(request.build())
    }
}
