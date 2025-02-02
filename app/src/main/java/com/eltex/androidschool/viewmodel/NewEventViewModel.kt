package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.Instant

class NewEventViewModel(
    private val repository: EventRepository,
    private val id: Long = 0,
) : ViewModel() {
    private val _state = MutableStateFlow(NewEventUiState())
    val state = _state.asStateFlow()

    fun addEvent(content: String, datetime: Instant) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val event = repository.saveEvent(id, content, datetime)
                _state.update { it.copy(event = event, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun consumeError() {
        _state.update { it.copy(status = Status.Idle) }
    }
}