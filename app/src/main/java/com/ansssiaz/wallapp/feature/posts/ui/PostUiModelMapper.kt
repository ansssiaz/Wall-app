package com.ansssiaz.wallapp.feature.posts.ui

import com.ansssiaz.wallapp.feature.posts.data.Post
import com.ansssiaz.wallapp.utils.DateFormatter
import javax.inject.Inject

class PostUiModelMapper @Inject constructor(private val dateFormatter: DateFormatter){

    fun map(post: Post): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            content = content,
            author = author,
            authorAvatar = authorAvatar,
            published = dateFormatter.formatDate(published),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
        )
    }
}