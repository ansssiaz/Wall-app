package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import org.threeten.bp.Instant

class NetworkEventRepository(private val api: EventsApi) : EventRepository {

    override suspend fun getEvents(): List<Event> = api.getEvents()

    override suspend fun saveEvent(id: Long, content: String, datetime: Instant): Event = api.save(
        Event(
            id = id,
            content = content,
            datetime = datetime
        )
    )

    override suspend fun delete(id: Long) = api.delete(id)

    override suspend fun like(id: Long): Event = api.like(id)

    override suspend fun deleteLike(id: Long): Event = api.deleteLike(id)

    override suspend fun participate(id: Long): Event = api.participate(id)

    override suspend fun deleteParticipation(id: Long): Event = api.deleteParticipation(id)
}