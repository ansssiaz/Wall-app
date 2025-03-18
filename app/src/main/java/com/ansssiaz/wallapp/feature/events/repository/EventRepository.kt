package com.ansssiaz.wallapp.feature.events.repository
import com.ansssiaz.wallapp.feature.events.data.Event
import org.threeten.bp.Instant

interface EventRepository {
    suspend fun getLatest(count: Int): List<Event>
    suspend fun getBefore(id: Long, count: Int): List<Event>
    suspend fun like(id : Long): Event
    suspend fun deleteLike(id: Long): Event
    suspend fun participate(id : Long): Event
    suspend fun deleteParticipation(id: Long): Event
    suspend fun saveEvent(id: Long, content: String, datetime: Instant): Event
    suspend fun delete(id: Long)
}