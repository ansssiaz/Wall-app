package com.eltex.androidschool.repository
import com.eltex.androidschool.model.Event
import org.threeten.bp.Instant

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun like(id : Long): Event
    suspend fun deleteLike(id: Long): Event
    suspend fun participate(id : Long): Event
    suspend fun deleteParticipation(id: Long): Event
    suspend fun saveEvent(id: Long, content: String, datetime: Instant): Event
    suspend fun delete(id: Long)
}