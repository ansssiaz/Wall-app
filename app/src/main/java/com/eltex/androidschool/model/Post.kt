package com.eltex.androidschool.model

data class Post(
    val id: Long = 0L,
    val content: String = "",
    val author: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
)
