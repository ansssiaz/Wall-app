package com.eltex.androidschool.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardPostBinding
import com.eltex.androidschool.model.Post

class PostViewHolder(
    private val binding: CardPostBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(payload: PostPayload) {
        if (payload.like != null) {
            updateLike(payload.like)
        }
    }

    fun bind(post: Post) {
        binding.content.text = post.content
        updateLike(post.likedByMe)
        binding.author.text = post.author
        binding.published.text = post.published
        binding.authorInitials.text = post.author.take(1)
    }

    @SuppressLint("SetTextI18n")
    private fun updateLike(likedByMe: Boolean) {
        binding.like.setIconResource(
            if (likedByMe) {
                R.drawable.liked_icon
            } else {
                R.drawable.like_icon_border
            }
        )
        binding.like.text = if (likedByMe) {
            1
        } else {
            0
        }
            .toString()
    }
}
