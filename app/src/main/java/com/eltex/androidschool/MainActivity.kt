package com.eltex.androidschool

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.Type
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer { EventViewModel(InMemoryEventRepository()) }
            }
        }

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { bindEvent(binding, it.event) }
            .launchIn(lifecycleScope)

        binding.like.setOnClickListener {
            viewModel.like()
        }

        binding.share.setOnClickListener {
            toast(R.string.not_implemented)
        }

        binding.participate.setOnClickListener {
            viewModel.participate()
        }

        binding.menu.setOnClickListener {
            toast(R.string.not_implemented)
        }
    }
}

@SuppressLint("SetTextI18n")
private fun bindEvent(binding: ActivityMainBinding, event: Event) {
    binding.content.text = event.content
    binding.like.setIconResource(
        if (event.likedByMe) {
            R.drawable.liked_icon
        } else {
            R.drawable.like_icon_border
        }
    )
    binding.author.text = event.author
    binding.published.text = event.published
    binding.datetime.text = event.datetime
    binding.avatarInitials.text = event.author.take(1)
    binding.type.setText(
        when (event.type) {
            Type.OFFLINE -> R.string.offline
            Type.ONLINE -> R.string.online
        }
    )
    binding.like.text = if (event.likedByMe) {
        1
    } else {
        0
    }
        .toString()
    binding.participate.text = if (event.participatedByMe) {
        1
    } else {
        0
    }
        .toString()
}