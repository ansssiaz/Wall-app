package com.eltex.androidschool.repository

import com.eltex.androidschool.BuildConfig
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.Callback
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.internal.EMPTY_REQUEST
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.Instant
import java.io.IOException
import java.util.concurrent.TimeUnit

class NetworkEventRepository : EventRepository {

    private companion object {
        val JSON_TYPE = "application/json".toMediaType()
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
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
        .addInterceptor({ chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Api-Key", BuildConfig.API_KEY)
                    .addHeader("Authorization", BuildConfig.AUTHORIZATION)
                    .build()
            )
        }
        )
        .build()

    override fun getEvents(callback: Callback<List<Event>>) {
        val call = client.newCall(
            Request.Builder()
                .url("https://eltex-android.ru/api/events")
                .build()
        )

        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(
                                json.decodeFromString<List<Event>>(
                                    requireNotNull(
                                        response.body?.string()
                                    )
                                )
                            )
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        throw RuntimeException("Response code is ${response.code}")
                    }
                }
            }
        )
    }

    override fun saveEvent(id: Long, content: String, callback: Callback<Event>) {
        val today = Instant.now().toString()

        val request = Request.Builder()
            .post(
                json.encodeToString(
                    Event(
                        id = id,
                        content = content,
                        published = today,
                        datetime = today
                    )
                ).toRequestBody(JSON_TYPE)
            )
            .url("https://eltex-android.ru/api/events")
            .build()

        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun deleteById(id: Long, callback: Callback<Unit>) {
        val request = Request.Builder()
            .delete()
            .url("https://eltex-android.ru/api/events/$id")
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        try {
                            callback.onSuccess(Unit)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun like(id: Long, callback: Callback<Event>) {
        val request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("https://eltex-android.ru/api/events/$id/likes")
            .build()

        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun deleteLike(id: Long, callback: Callback<Event>) {
        val request = Request.Builder()
            .delete()
            .url("https://eltex-android.ru/api/events/$id/likes")
            .build()

        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun participate(id: Long, callback: Callback<Event>) {
        val request = Request.Builder()
            .post(EMPTY_REQUEST)
            .url("https://eltex-android.ru/api/events/$id/participants")
            .build()

        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }

    override fun deleteParticipation(id: Long, callback: Callback<Event>) {
        val request = Request.Builder()
            .delete()
            .url("https://eltex-android.ru/api/events/$id/participants")
            .build()

        val call = client.newCall(request)
        call.enqueue(
            object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        try {
                            callback.onSuccess(json.decodeFromString(body.string()))
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code}"))
                    }
                }
            }
        )
    }
}