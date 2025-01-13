package com.eltex.androidschool.model

import kotlinx.serialization.Serializable
import java.util.Collections.emptyList

@Serializable
data class Event(
    val id: Long = 0L,
    val authorId: Long = 0L,
    val author: String = "",
    val authorAvatar: String? = "",
    val content: String = "",
    val published: String = "",
    val datetime: String = "",
    val type: Type = Type.OFFLINE,
    val likeOwnerIds: List<Long?> = emptyList<Long>(),
    val likedByMe: Boolean = false,
    val participantIds: List<Long?> = emptyList<Long>(),
    val participatedByMe: Boolean = false,
    val link: String? = "",
    val likes: Int = 0,
    val participants: Int = 0
)

enum class Type {
    OFFLINE,
    ONLINE
}