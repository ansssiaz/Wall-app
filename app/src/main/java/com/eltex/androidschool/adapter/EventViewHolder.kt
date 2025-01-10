package com.eltex.androidschool.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.CardEventBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Type

class EventViewHolder(
    private val binding: CardEventBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(payload: EventPayload) {
        if (payload.like != null) {
            updateLike(payload.like)
        }
        if (payload.participate != null) {
            updateParticipate(payload.participate)
        }
    }

    fun bind(event: Event) {
        binding.content.text = event.content
        updateLike(event.likedByMe)
        updateParticipate(event.participatedByMe)
        binding.author.text = event.author
        binding.published.text = event.published
        binding.datetime.text = event.datetime
        binding.avatarInitials.text = event.author.take(1)
        binding.link.text = event.link
        binding.type.setText(
            when (event.type) {
                Type.OFFLINE -> R.string.offline
                Type.ONLINE -> R.string.online
            }
        )
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

    @SuppressLint("SetTextI18n")
    private fun updateParticipate(participatedByMe: Boolean) {
        binding.participate.text = if (participatedByMe) {
            1
        } else {
            0
        }
            .toString()
    }
}
