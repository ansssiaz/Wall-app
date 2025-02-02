package com.eltex.androidschool.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitFactory {
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }
    val INSTANCE: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClientFactory.INSTANCE)
            .baseUrl("https://eltex-android.ru/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}