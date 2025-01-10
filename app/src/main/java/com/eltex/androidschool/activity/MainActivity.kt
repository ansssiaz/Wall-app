package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.itemdecoration.DateDecoration
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.model.Event
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer { EventViewModel(InMemoryEventRepository()) }
            }
        }

        /* val adapter = EventsAdapter(likeClickListener = {
             viewModel.likeById(it.id)
         }, participateClickListener =
         {
             viewModel.participate(it.id)
         })*/

        val adapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClicked(event: Event) {
                    viewModel.likeById(event.id)
                }

                override fun onParticipateClicked(event: Event) {
                    viewModel.participate(event.id)
                }

                override fun onShareClicked(event: Event) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, event.content)
                        .setType("text/plain")
                    val chooser = Intent.createChooser(intent, null)
                    startActivity(chooser)
                }

                override fun onDeleteClicked(event: Event) {
                    viewModel.deleteById(event.id)
                }
            }
        )
        val newEventContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
                if (content != null) {
                    viewModel.addEvent(content)
                }
            }
        binding.addEvent.setOnClickListener {
            newEventContract.launch(Intent(this, NewEventActivity::class.java))
        }

/*        binding.root.adapter = adapter
        binding.root.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.root.addItemDecoration(
            DateDecoration(
                getEvent = { viewModel.uiState.value.events[it] },
                context = this
            )
        )*/

        binding.list.adapter = adapter
        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )
        binding.list.addItemDecoration(
            DateDecoration(
                //getEvent = { viewModel.uiState.value.events[it] },
                getEvent = { position ->
                    if (position in 0 until viewModel.uiState.value.events.size) {
                        viewModel.uiState.value.events[position]
                    } else {
                        null
                    }
                },
                context = this
            )
        )

        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach {
                adapter.submitList(it.events)
            }
            .launchIn(lifecycleScope)
    }
}