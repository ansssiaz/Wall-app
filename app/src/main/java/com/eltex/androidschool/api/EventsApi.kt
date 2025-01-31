package com.eltex.androidschool.api

import com.eltex.androidschool.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsApi {
    @GET("api/events")
    fun getEvents(): Single<List<Event>>

    @POST("api/events/{id}/likes")
    fun like(@Path("id") id: Long): Single<Event>

    @DELETE("api/events/{id}/likes")
    fun deleteLike(@Path("id") id: Long): Single<Event>

    @POST("api/events/{id}/participants")
    fun participate(@Path("id") id: Long): Single<Event>

    @DELETE("api/events/{id}/participants")
    fun deleteParticipation(@Path("id") id: Long): Single<Event>

    @POST("api/events")
    fun save(@Body event: Event): Single<Event>

    @DELETE("api/events/{id}")
    fun delete(@Path("id") id: Long): Completable

    companion object {
        val INSTANCE: EventsApi by lazy {
            RetrofitFactory.INSTANCE.create()
        }
    }
}