package com.github.nullsafe.watchwise.core.data.interceptor

import com.github.nullsafe.watchwise.core.BuildConfig
import com.github.nullsafe.watchwise.core.data.datastore.UserPreferencesManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val language = runBlocking {
            userPreferencesManager.userPreferencesFlow.first().language
        }

        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.THE_MOVIE_DATABASE_API_KEY)
            .addQueryParameter("language", language)
            .build()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.THE_MOVIE_DATABASE_API_KEY}")
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}