package com.ansssiaz.wallapp.feature.events.data

import com.ansssiaz.wallapp.utils.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.Instant
import java.util.Collections.emptyList

@Serializable
data class Event(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("authorId")
    val authorId: Long = 0L,
    @SerialName("author")
    val author: String = "",
    @SerialName("authorAvatar")
    val authorAvatar: String? = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("datetime")
    @Serializable(InstantSerializer::class)
    val datetime: Instant = Instant.now(),
    @SerialName("type")
    val type: Type = Type.OFFLINE,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: List<Long?> = emptyList<Long>(),
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("participantsIds")
    val participantsIds: List<Long?> = emptyList<Long>(),
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
    @SerialName("link")
    val link: String? = "",
)

enum class Type {
    OFFLINE,
    ONLINE
}