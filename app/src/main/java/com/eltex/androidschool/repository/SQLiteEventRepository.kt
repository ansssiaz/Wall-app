package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.entity.EventEntity
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class SQLiteEventRepository(private val dao: EventDao) : EventRepository {
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")

    override fun getEvents(): Flow<List<Event>> = dao.getAll()
        .map {
            it.map(EventEntity::toEvent)
        }

    override fun like(id: Long) {
        dao.likeById(id)
    }

    override fun participate(id: Long) {
        dao.participateById(id)
    }

    override fun addEvent(content: String) {
        val startDate = LocalDateTime.now()
        dao.save(
            EventEntity.fromEvent(
                Event(
                    author = "Student",
                    content = content,
                    published = startDate.format(formatter),
                    datetime = startDate.format(formatter)
                )
            )
        )
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    override fun editById(id: Long, content: String) {
        val existingEvent = dao.getEventById(id).toEvent()
        val updatedEvent = existingEvent.copy(content = content)
        dao.save(EventEntity.fromEvent(updatedEvent))
    }
}