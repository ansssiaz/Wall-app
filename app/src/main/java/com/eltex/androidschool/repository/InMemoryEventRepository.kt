package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class InMemoryEventRepository : EventRepository {
    private val startDate = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    private val state = MutableStateFlow(
        listOf(
            Event(
                id = 1L,
                authorId = 1L,
                author = "Author 1",
                published = startDate.format(formatter),
                type = Type.OFFLINE,
                datetime = startDate.format(formatter),
                content = "№1. Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
                link = "https://m2.material.io/components/cards"
            ),
            Event(
                id = 2L,
                authorId = 2L,
                author = "Author 2",
                published = startDate.format(formatter),
                type = Type.OFFLINE,
                datetime = startDate.format(formatter),
                content = "№2. Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
                link = "https://m2.material.io/components/cards"
            ),

            Event(
                id = 3L,
                authorId = 3L,
                author = "Author 3",
                published = startDate.minusDays(1).format(formatter),
                type = Type.OFFLINE,
                datetime = startDate.minusDays(1).format(formatter),
                content = "№3. Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
                link = "https://m2.material.io/components/cards"
            ),
            Event(
                id = 4L,
                authorId = 4L,
                author = "Author 4",
                published = startDate.minusDays(2).format(formatter),
                type = Type.OFFLINE,
                datetime = startDate.minusDays(2).format(formatter),
                content = "№4. Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
                link = "https://m2.material.io/components/cards"
            )
        )
    )

    private var nextId = state.value.last().id

    override fun getEvents(): Flow<List<Event>> = state.asStateFlow()

    override fun like(id: Long) {
        state.update {
            it.map { event ->
                if (event.id == id) {
                    event.copy(likedByMe = !event.likedByMe)
                } else {
                    event
                }
            }
        }
    }

    override fun participate(id: Long) {
        state.update {
            it.map { event ->
                if (event.id == id) {
                    event.copy(participatedByMe = !event.participatedByMe)
                } else {
                    event
                }
            }
        }
    }

    override fun addEvent(content: String) {
        state.update { events ->
            buildList(capacity = events.size + 1) {
                add(Event(id = ++nextId, content = content, author = "Student", published = startDate.format(formatter)))
                addAll(events)
            }
        }
    }
    override fun deleteById(id: Long) {
        state.update { events ->
            events.filter { it.id != id }
        }
    }
}