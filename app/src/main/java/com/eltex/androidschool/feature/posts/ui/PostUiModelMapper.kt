package com.eltex.androidschool.feature.posts.ui

import com.eltex.androidschool.feature.posts.data.Post
import org.threeten.bp.ZoneId.systemDefault
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class PostUiModelMapper @Inject constructor(){
    companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    fun map(post: Post): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            content = content,
            author = author,
            authorAvatar = authorAvatar,
            published = FORMATTER.format(published.atZone(systemDefault())),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
        )
    }
}