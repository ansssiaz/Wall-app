package com.ansssiaz.wallapp.di

import com.ansssiaz.wallapp.BuildConfig
import com.ansssiaz.wallapp.api.RetrofitFactory
import com.ansssiaz.wallapp.feature.events.api.EventsApi
import com.ansssiaz.wallapp.feature.posts.api.PostsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideOkHttp() = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .addInterceptor {
            it.proceed(
                it.request()
                    .newBuilder()
                    .addHeader("Api-Key", BuildConfig.API_KEY)
                    .addHeader("Authorization", BuildConfig.AUTHORIZATION)
                    .build()
            )
        }
        .let {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            } else {
                it
            }
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient,)= RetrofitFactory.createRetrofit(okHttpClient)

    @Provides
    fun providePostsApi(retrofit: Retrofit): PostsApi = retrofit.create()

    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi = retrofit.create()
}