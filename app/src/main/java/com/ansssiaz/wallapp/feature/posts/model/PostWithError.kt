package com.ansssiaz.wallapp.feature.posts.model

import com.ansssiaz.wallapp.feature.posts.ui.PostUiModel

data class PostWithError(
    val post: PostUiModel,
    val throwable: Throwable,

    )
