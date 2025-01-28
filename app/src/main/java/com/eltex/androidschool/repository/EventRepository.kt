package com.eltex.androidschool.repository
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.Callback

interface EventRepository {
    fun getEvents(callback: Callback<List<Event>>)
    fun like(id : Long, callback: Callback<Event>)
    fun deleteLike(id: Long, callback: Callback<Event>)
    fun participate(id : Long, callback: Callback<Event>)
    fun deleteParticipation(id: Long, callback: Callback<Event>)
    fun saveEvent(id: Long, content: String, callback: Callback<Event>)
    fun delete(id: Long, callback: Callback<Unit>)
}