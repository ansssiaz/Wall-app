package com.eltex.androidschool.feature.newpost.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.feature.posts.repository.PostRepository
import com.eltex.androidschool.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPostViewModel(
    private val repository: PostRepository,
    private val id: Long = 0,
) : ViewModel() {
    private val _state = MutableStateFlow(NewPostUiState())
    val state = _state.asStateFlow()

    fun addPost(content: String) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val post = repository.savePost(id, content)
                _state.update { it.copy(post = post, status = Status.Idle) }
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