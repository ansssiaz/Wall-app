package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryEventRepository : EventRepository {
    private val state = MutableStateFlow(
        List(100) {
            Event(
                id = it.toLong() + 1L,
                authorId = 1L,
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                type = Type.OFFLINE,
                datetime = "16.05.22 12:00",
                content = "№$it Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
                link = "https://m2.material.io/components/cards",
            )
        }
    )

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
}