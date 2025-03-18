package com.ansssiaz.wallapp.feature.newpost.viewmodel

import com.ansssiaz.wallapp.feature.posts.data.Post
import com.ansssiaz.wallapp.utils.Status

data class NewPostUiState(
    val post: Post? = null,
    val status: Status = Status.Idle,
)

