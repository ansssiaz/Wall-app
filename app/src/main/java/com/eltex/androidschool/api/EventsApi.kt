package com.eltex.androidschool.api

import com.eltex.androidschool.model.Event
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsApi {
    @GET("api/events")
    suspend fun getEvents(): List<Event>

    @POST("api/events/{id}/likes")
    suspend fun like(@Path("id") id: Long): Event

    @DELETE("api/events/{id}/likes")
    suspend fun deleteLike(@Path("id") id: Long): Event

    @POST("api/events/{id}/participants")
    suspend fun participate(@Path("id") id: Long): Event

    @DELETE("api/events/{id}/participants")
    suspend fun deleteParticipation(@Path("id") id: Long): Event

    @POST("api/events")
    suspend fun save(@Body event: Event): Event

    @DELETE("api/events/{id}")
    suspend fun delete(@Path("id") id: Long)

    companion object {
        val INSTANCE: EventsApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }
}