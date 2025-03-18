package com.ansssiaz.wallapp.feature.newevent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ansssiaz.wallapp.utils.Status
import com.ansssiaz.wallapp.feature.events.repository.EventRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.Instant

@HiltViewModel(assistedFactory = NewEventViewModel.ViewModelFactory::class)
class NewEventViewModel @AssistedInject constructor(
    private val repository: EventRepository,
    @Assisted private val id: Long = 0,
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

    @AssistedFactory
    interface ViewModelFactory {
        fun create(id: Long): NewEventViewModel
    }
}