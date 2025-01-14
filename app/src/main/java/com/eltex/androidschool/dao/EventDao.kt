package com.eltex.androidschool.dao

import com.eltex.androidschool.model.Event
interface EventDao {
    fun getAll(): List<Event>
    fun likeById(id: Long): Event
    fun participateById(id : Long): Event
    fun save(event: Event): Event
    fun deleteById(id: Long)
    fun getEventById(id: Long): Event
}