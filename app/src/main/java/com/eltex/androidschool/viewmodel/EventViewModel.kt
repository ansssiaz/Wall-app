package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class EventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        repository.getEvents()
            .onEach { events ->
                _uiState.update {
                    it.copy(events = events)
                }
            }
            .launchIn(viewModelScope)
    }

    fun likeById(id: Long) {
        repository.like(id)
    }

    fun participate(id: Long) {
        repository.participate(id)
    }

    fun getEventsList():List<Event> {
        return repository.getEvents().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList()).value
    }
}
