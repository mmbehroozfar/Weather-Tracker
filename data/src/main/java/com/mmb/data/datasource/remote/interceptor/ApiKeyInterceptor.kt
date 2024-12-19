package com.mmb.data.datasource.remote.interceptor

import com.mmb.data.BuildConfig
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val urlWithApiKey = originalUrl.newBuilder()
            .addQueryParameter(QUERY_PARAMETER_KEY, BuildConfig.API_KEY)
            .build()

        val requestWithApiKey = originalRequest.newBuilder()
            .url(urlWithApiKey)
            .build()

        return chain.proceed(requestWithApiKey)
    }

    private companion object {
        private const val QUERY_PARAMETER_KEY = "key"
    }
}