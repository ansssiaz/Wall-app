package com.eltex.androidschool.viewmodel

import androidx.lifecycle.ViewModel
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.PostRepository
import com.eltex.androidschool.utils.Callback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewPostViewModel(
    private val repository: PostRepository,
    private val id: Long = 0,
) : ViewModel() {
    private val _state = MutableStateFlow(NewPostUiState())
    val state = _state.asStateFlow()

    fun addPost(content: String) {
        _state.update { it.copy(status = Status.Loading) }
        repository.savePost(id, content, object : Callback<Post> {
            override fun onSuccess(data: Post) {
                _state.update { it.copy(post = data, status = Status.Idle) }
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