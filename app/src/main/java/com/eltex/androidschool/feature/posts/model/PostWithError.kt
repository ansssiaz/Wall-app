package com.eltex.androidschool.feature.posts.model

import com.eltex.androidschool.feature.posts.ui.PostUiModel

data class PostWithError(
    val post: PostUiModel,
    val throwable: Throwable,

    )
