package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long = 0,
) : ViewModel() {
    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()

    fun addEvent(content: String) {
        _state.update { it.copy(status = Status.Loading) }
        repository.saveEvent(id, content, object : Callback<Event> {
            override fun onSuccess(data: Event) {
                _state.update { it.copy(event = data, status = Status.Idle) }
            }

            override fun onError(exception: Throwable) {
                _state.update { it.copy(status = Status.Error(exception)) }
            }
        }
        )
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}