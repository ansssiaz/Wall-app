package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
    fun like(id : Long)
    fun participate(id : Long)
    fun addEvent(content: String)
    fun deleteById(id: Long)
    fun editById (id: Long, content: String)
}