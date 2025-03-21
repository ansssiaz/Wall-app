package com.ansssiaz.wallapp.feature.posts.ui

data class PostUiModel(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val authorAvatar: String? = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val likes: Int = 0,
)