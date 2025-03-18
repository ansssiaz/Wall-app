package com.ansssiaz.wallapp.feature.posts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ansssiaz.wallapp.R
import com.ansssiaz.wallapp.adapter.ErrorViewHolder
import com.ansssiaz.wallapp.databinding.CardPostBinding
import com.ansssiaz.wallapp.databinding.ItemErrorBinding
import com.ansssiaz.wallapp.databinding.PostSkeletonBinding
import com.ansssiaz.wallapp.feature.posts.ui.PostPagingModel
import com.ansssiaz.wallapp.feature.posts.ui.PostUiModel
import com.ansssiaz.wallapp.ui.PagingModel

class PostsAdapter(
    private val listener: PostListener,
) : ListAdapter<PostPagingModel, RecyclerView.ViewHolder>(PostPagingDiffCallback()) {

    interface PostListener {
        fun onLikeClicked(post: PostUiModel)
        fun onShareClicked(post: PostUiModel)
        fun onDeleteClicked(post: PostUiModel)
        fun onEditClicked(post: PostUiModel)
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is PagingModel.Data -> R.layout.card_post
        is PagingModel.Error -> R.layout.item_error
        PagingModel.PostSkeleton -> R.layout.post_skeleton
        PagingModel.EventSkeleton -> 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.card_post -> createPostViewHolder(parent)
            R.layout.item_error -> createErrorViewHolder(parent)
            R.layout.post_skeleton -> createPostSkeletonViewHolder(parent)
            else -> error("Unknown viewType: $viewType")
        }
    }

    private fun createPostSkeletonViewHolder(parent: ViewGroup): PostSkeletonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostSkeletonBinding.inflate(layoutInflater, parent, false)
        return PostSkeletonViewHolder(binding)
    }

    private fun createErrorViewHolder(parent: ViewGroup): ErrorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemErrorBinding.inflate(layoutInflater, parent, false)
        return ErrorViewHolder(binding)
    }

    private fun createPostViewHolder(parent: ViewGroup): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPostBinding.inflate(layoutInflater, parent, false)
        val viewHolder = PostViewHolder(binding)

        binding.like.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
            item?.value?.let(listener::onLikeClicked)
        }

        binding.share.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
            item?.value?.let(listener::onShareClicked)
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.action_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.delete -> {
                            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
                            item?.value?.let(listener::onDeleteClicked)
                            true
                        }

                        R.id.edit -> {
                            val item = getItem(viewHolder.adapterPosition) as? PagingModel.Data
                            item?.value?.let(listener::onEditClicked)
                            true
                        }

                        else -> false
                    }
                }
                show()
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>,
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is PostPayload) {
                    (holder as? PostViewHolder)?.bind(it)
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is PagingModel.Data -> (holder as PostViewHolder).bind(item.value)
            is PagingModel.Error -> (holder as ErrorViewHolder).bind(item.reason)
            else -> Unit
        }
    }
}