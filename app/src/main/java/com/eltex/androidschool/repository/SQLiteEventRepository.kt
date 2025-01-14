package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class SQLiteEventRepository(private val dao: EventDao) : EventRepository {
    private val state = MutableStateFlow(readEvents())
    private val startDate = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")

    private fun readEvents(): List<Event> = dao.getAll()

    override fun getEvents(): Flow<List<Event>> = state

    override fun like(id: Long) {
        dao.likeById(id)
        state.update { readEvents() }
    }

    override fun participate(id: Long) {
        dao.participateById(id)
        state.update { readEvents() }
    }

    override fun addEvent(content: String) {
        dao.save(
            Event(
                author = "Student",
                content = content,
                published = startDate.format(formatter),
                datetime = startDate.format(formatter)
            )
        )
        state.update { readEvents() }
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
        state.update { readEvents() }
    }

    override fun editById(id: Long, content: String) {
        val existingEvent = dao.getEventById(id)
        val updatedEvent = existingEvent.copy(content = content)
        dao.save(updatedEvent)
        state.update { readEvents() }
    }
}