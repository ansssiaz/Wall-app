package com.eltex.androidschool.model

data class EventUiModel(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorAvatar: String? = "",
    val content: String = "",
    val published: String = "",
    val datetime: String = "",
    val type: Type = Type.OFFLINE,
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val link: String? = "",
    val likes: Int = 0,
    val participants: Int = 0
)
