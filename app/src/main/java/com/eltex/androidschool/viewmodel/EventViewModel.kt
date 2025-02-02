package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mapper.EventUiModelMapper
import com.eltex.androidschool.model.EventUiModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventViewModel(
    private val repository: EventRepository,
    private val mapper: EventUiModelMapper,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val posts: List<EventUiModel> = repository.getEvents()
                    .map {
                        mapper.map(it)
                    }

                _uiState.update {
                    it.copy(events = posts, status = Status.Idle)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }

        }
    }

    fun consumeError() {
        _uiState.update {
            if (it.status is Status.Error) {
                it.copy(status = Status.Idle)
            } else {
                it
            }
        }
    }

    fun like(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val likedEvent =
                    if (!event.likedByMe) repository.like(event.id) else repository.deleteLike(event.id)

                val likedEventUiModel = mapper.map(likedEvent)

                _uiState.update { state ->
                    state.copy(
                        events = state.events.orEmpty().map {
                            if (it.id == event.id) {
                                likedEventUiModel
                            } else {
                                it
                            }
                        },
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun participate(event: EventUiModel) {
        _uiState.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val participatedEvent =
                    if (!event.participatedByMe) repository.participate(event.id) else repository.deleteParticipation(
                        event.id
                    )

                val participatedEventUiModel = mapper.map(participatedEvent)

                _uiState.update { state ->
                    state.copy(
                        events = state.events.orEmpty().map {
                            if (it.id == event.id) {
                                participatedEventUiModel
                            } else {
                                it
                            }
                        },
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun deleteById(id: Long) {
        _uiState.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                repository.delete(id)

                _uiState.update { state ->
                    state.copy(
                        events = state.events.orEmpty().filter { it.id != id },
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }
}
