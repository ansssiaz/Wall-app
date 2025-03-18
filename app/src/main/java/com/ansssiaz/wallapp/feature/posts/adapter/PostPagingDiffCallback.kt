package com.ansssiaz.wallapp.feature.posts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ansssiaz.wallapp.feature.posts.ui.PostPagingModel
import com.ansssiaz.wallapp.ui.PagingModel

class PostPagingDiffCallback : DiffUtil.ItemCallback<PostPagingModel>() {
    private val delegate = PostDiffCallback()

    override fun areItemsTheSame(oldItem: PostPagingModel, newItem: PostPagingModel): Boolean {
        if (oldItem::class != newItem::class) return false
        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.areItemsTheSame(oldItem.value, newItem.value)
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: PostPagingModel, newItem: PostPagingModel): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: PostPagingModel, newItem: PostPagingModel): Any? {
        if (oldItem::class != newItem::class) {
            return null
        }
        return if (oldItem is PagingModel.Data && newItem is PagingModel.Data) {
            delegate.getChangePayload(oldItem.value, newItem.value)
        } else {
            null
        }
    }
}