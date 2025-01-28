package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.threeten.bp.Instant

class NetworkEventRepository(private val api: EventsApi) : EventRepository {

    override fun getEvents(callback: com.eltex.androidschool.utils.Callback<List<Event>>) {
        api.getEvents().enqueue(
            object : Callback<List<Event>> {
                override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun saveEvent(
        id: Long,
        content: String,
        callback: com.eltex.androidschool.utils.Callback<Event>
    ) {
        val today = Instant.now().toString()

        api.save(
            Event(
                id = id,
                content = content,
                published = today,
                datetime = today
            )
        ).enqueue(
            object : Callback<Event> {
                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun delete(id: Long, callback: com.eltex.androidschool.utils.Callback<Unit>) {
        api.delete(id).enqueue(
            object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun like(id: Long, callback: com.eltex.androidschool.utils.Callback<Event>) {
        api.like(id).enqueue(
            object : Callback<Event> {
                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun deleteLike(id: Long, callback: com.eltex.androidschool.utils.Callback<Event>) {
        api.deleteLike(id).enqueue(
            object : Callback<Event> {
                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun participate(id: Long, callback: com.eltex.androidschool.utils.Callback<Event>) {
        api.participate(id).enqueue(
            object : Callback<Event> {
                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun deleteParticipation(
        id: Long,
        callback: com.eltex.androidschool.utils.Callback<Event>
    ) {
        api.deleteParticipation(id).enqueue(
            object : Callback<Event> {
                override fun onFailure(call: Call<Event>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }
}