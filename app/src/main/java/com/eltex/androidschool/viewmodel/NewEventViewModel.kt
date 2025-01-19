package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.repository.EventRepository

class NewEventViewModel(
    private val repository: EventRepository,
    private val eventId: Long = 0,
) : ViewModel() {
    fun addEvent(content: String) {
        repository.addEvent(content)
    }

    fun editEvent(content: String) {
        repository.editById(eventId, content)
    }
}