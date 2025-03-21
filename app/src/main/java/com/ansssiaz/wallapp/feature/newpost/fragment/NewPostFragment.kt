package com.ansssiaz.wallapp.feature.newpost.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ansssiaz.wallapp.R
import com.ansssiaz.wallapp.databinding.FragmentNewPostBinding
import com.ansssiaz.wallapp.feature.newpost.viewmodel.NewPostViewModel
import com.ansssiaz.wallapp.feature.toolbar.viewmodel.ToolbarViewModel
import com.ansssiaz.wallapp.utils.getErrorText
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewPostFragment : Fragment() {
    companion object {
        const val ARG_POST_ID = "ARG_POST_ID"
        const val ARG_CONTENT = "ARG_CONTENT"
        const val POST_CREATED_RESULT = "POST_CREATED_RESULT"
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

        val binding = FragmentNewPostBinding.inflate(layoutInflater)
        val id = arguments?.getLong(ARG_POST_ID) ?: 0L
        val initialContent = arguments?.getString(ARG_CONTENT) ?: ""
        binding.content.setText(initialContent)

        val title =
            if (id != 0L) getString(R.string.edit_post) else getString(R.string.new_post)

        val viewModel by viewModels<NewPostViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras
                    .withCreationCallback<NewPostViewModel.ViewModelFactory> { factory ->
                    factory.create(
                        id
                    )
                }

            }
        )

        val toolbarViewModel by activityViewModels<ToolbarViewModel>()
        toolbarViewModel.setTitle(title)
        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()
                if (content.isBlank()) {
                    Toast.makeText(requireContext(), R.string.empty_event_error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.addPost(content)
                }
                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                if (it.post != null) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        POST_CREATED_RESULT,
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