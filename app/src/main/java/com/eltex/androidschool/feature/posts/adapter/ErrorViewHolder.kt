package com.eltex.androidschool.feature.posts.adapter

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.utils.getErrorText

class ErrorViewHolder(
    private val binding: ItemErrorBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(throwable: Throwable) {
        binding.errorText.text = throwable.getErrorText(binding.root.context)
    }
}