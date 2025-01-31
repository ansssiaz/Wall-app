package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.threeten.bp.Instant
import java.util.concurrent.TimeUnit

class NetworkEventRepository(private val api: EventsApi) : EventRepository {

    override fun getEvents(): Single<List<Event>> = api.getEvents()

    override fun saveEvent(id: Long, content: String): Single<Event> {
        val today = Instant.now()

        return Single.just(
            Event(
                id = id,
                content = content,
                published = today,
                datetime = today
            )
        )
            .delay(1, TimeUnit.SECONDS)
            .flatMap { event -> api.save(event) }
    }

    override fun delete(id: Long): Completable = api.delete(id)

    override fun like(id: Long): Single<Event> = api.like(id)

    override fun deleteLike(id: Long): Single<Event> = api.deleteLike(id)

    override fun participate(id: Long): Single<Event> = api.participate(id)

    override fun deleteParticipation(id: Long): Single<Event> = api.deleteParticipation(id)
}