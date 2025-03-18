package com.ansssiaz.wallapp.feature.posts.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ansssiaz.wallapp.R
import com.ansssiaz.wallapp.databinding.CardPostBinding
import com.ansssiaz.wallapp.feature.posts.ui.PostUiModel

class PostViewHolder(
    private val binding: CardPostBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(payload: PostPayload) {
        if (payload.like != null && payload.likes != null) {
            updateLike(payload.like, payload.likes)
        }
    }

    fun bind(post: PostUiModel) {
        binding.content.text = post.content
        updateLike(post.likedByMe, post.likes)
        binding.author.text = post.author
        handleAvatarDisplay(post)
        binding.published.text = post.published
    }

    private fun handleAvatarDisplay(post: PostUiModel) {
        Glide.with(binding.root)
            .load(post.authorAvatar)
            .circleCrop()
            .placeholder(R.drawable.avatar_background)
            .into(binding.avatar)

        if (post.authorAvatar.isNullOrEmpty()) {
            binding.authorInitials.text = post.author.take(1)
        } else {
            binding.authorInitials.text = ""
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateLike(likedByMe: Boolean, likes: Int) {
        binding.like.setIconResource(
            if (likedByMe) {
                R.drawable.liked_icon
            } else {
                R.drawable.like_icon_border
            }
        )

        binding.like.text = likes
            .toString()
    }
}
