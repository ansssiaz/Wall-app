package com.eltex.androidschool.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.viewmodel.NewEventViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewEventFragment : Fragment() {
    companion object {
        const val ARG_EVENT_ID = "ARG_EVENT_ID"
        const val ARG_CONTENT = "ARG_CONTENT"
        const val EVENT_CREATED_RESULT = "EVENT_CREATED_RESULT"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    override fun onStart() {
        super.onStart()
        toolbarViewModel.setSaveVisibility(true)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.setSaveVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        toolbarViewModel.setTitle(getString(R.string.app_name))
    }

    override fun onCreateView(
        inflater:
        LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentNewEventBinding.inflate(layoutInflater)
        val id = arguments?.getLong(ARG_EVENT_ID) ?: 0L
        val initialContent = arguments?.getString(ARG_CONTENT) ?: ""
        binding.content.setText(initialContent)

        val title =
            if (id != 0L) R.string.edit_event_title else R.string.new_event_title

        val viewModel by viewModels<NewEventViewModel> {
            viewModelFactory {
                initializer {
                    NewEventViewModel(
                        repository = NetworkEventRepository(EventsApi.INSTANCE),
                        id = id,
                    )
                }
            }
        }

        val toolbarViewModel by activityViewModels<ToolbarViewModel>()
        toolbarViewModel.setTitle(getString(title))
        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()
                if (content.isBlank()) {
                    Toast.makeText(requireContext(), R.string.empty_event_error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addEvent(content)
                }
                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.event != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        EVENT_CREATED_RESULT,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                }

                it.status.throwableOrNull?.getErrorText(requireContext())?.let { errorText ->
                    Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
                    viewModel.consumeError()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}