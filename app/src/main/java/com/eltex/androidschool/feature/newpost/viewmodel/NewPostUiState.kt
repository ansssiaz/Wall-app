package com.eltex.androidschool.feature.newpost.viewmodel

import com.eltex.androidschool.feature.posts.data.Post
import com.eltex.androidschool.utils.Status

data class NewPostUiState(
    val post: Post? = null,
    val status: Status = Status.Idle,
)

