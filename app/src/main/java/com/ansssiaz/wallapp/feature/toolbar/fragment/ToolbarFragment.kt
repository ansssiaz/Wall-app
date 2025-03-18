package com.ansssiaz.wallapp.feature.toolbar.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ansssiaz.wallapp.R
import com.ansssiaz.wallapp.databinding.FragmentToolbarBinding
import com.ansssiaz.wallapp.feature.toolbar.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ToolbarFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentFragmentManager.beginTransaction()
            .setPrimaryNavigationFragment(this)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentToolbarBinding.inflate(inflater, container, false)

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()
        binding.toolbar.setupWithNavController(navController)

        val saveItem = binding.toolbar.menu.findItem(R.id.save)
        val toolbarViewModel by activityViewModels<ToolbarViewModel>()

        toolbarViewModel.showSave
            .onEach {
                saveItem.isVisible = it
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolbarViewModel.title.onEach { title ->
            binding.toolbar.title = title
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.toolbar.title = getString(R.string.app_name)

        saveItem.setOnMenuItemClickListener {
            toolbarViewModel.saveClicked(true)
            true
        }
        return binding.root
    }
}