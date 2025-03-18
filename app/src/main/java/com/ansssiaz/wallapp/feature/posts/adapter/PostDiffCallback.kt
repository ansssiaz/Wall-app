package com.ansssiaz.wallapp.feature.posts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ansssiaz.wallapp.feature.posts.ui.PostUiModel

class PostDiffCallback : DiffUtil.ItemCallback<PostUiModel>() {
    override fun areItemsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: PostUiModel, newItem: PostUiModel): Any? =
        PostPayload(
            like = newItem.likedByMe.takeIf {
                it != oldItem.likedByMe
            },
            likes = newItem.likes.takeIf {
                it != oldItem.likes
            },
        )
            .takeIf {
                it.isNotEmpty()
            }
}