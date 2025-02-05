package com.eltex.androidschool.feature.posts.data

import com.eltex.androidschool.utils.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.Instant

@Serializable
data class Post(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("content")
    val content: String = "",
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
)