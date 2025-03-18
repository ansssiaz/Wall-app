package com.ansssiaz.wallapp.feature.events.repository

import com.ansssiaz.wallapp.feature.events.api.EventsApi
import com.ansssiaz.wallapp.feature.events.data.Event
import org.threeten.bp.Instant
import javax.inject.Inject

class NetworkEventRepository @Inject constructor(private val api: EventsApi) : EventRepository {

    override suspend fun saveEvent(id: Long, content: String, datetime: Instant): Event = api.save(
        Event(
            id = id,
            content = content,
            datetime = datetime
        )
    )

    override suspend fun delete(id: Long) = api.delete(id)

    override suspend fun getLatest(count: Int): List<Event> = api.getLatest(count)

    override suspend fun getBefore(id: Long, count: Int): List<Event> = api.getBefore(id, count)

    override suspend fun like(id: Long): Event = api.like(id)

    override suspend fun deleteLike(id: Long): Event = api.deleteLike(id)

    override suspend fun participate(id: Long): Event = api.participate(id)

    override suspend fun deleteParticipation(id: Long): Event = api.deleteParticipation(id)
}