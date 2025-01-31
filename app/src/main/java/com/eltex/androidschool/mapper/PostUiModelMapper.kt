package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Post
import com.eltex.androidschool.model.PostUiModel
import org.threeten.bp.ZoneId.systemDefault
import org.threeten.bp.format.DateTimeFormatter

class PostUiModelMapper {
    companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    fun map(post: Post): PostUiModel = with(post) {
        PostUiModel(
            id = id,
            content = content,
            author = author,
            published = FORMATTER.format(published.atZone(systemDefault())),
            likedByMe = likedByMe,
            likes = likeOwnerIds.size,
        )
    }
}