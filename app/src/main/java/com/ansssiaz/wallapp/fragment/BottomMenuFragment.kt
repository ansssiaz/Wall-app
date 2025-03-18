package com.ansssiaz.wallapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ansssiaz.wallapp.R
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ansssiaz.wallapp.databinding.FragmentBottomMenuBinding

class BottomMenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentBottomMenuBinding.inflate(inflater, container, false)
        val eventsClickListener = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_bottomMenuFragment_to_newEventFragment)
        }
        val postsClickListener = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_bottomMenuFragment_to_newPostFragment)
        }

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()
        // Слушатель переключения вкладок
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postsFragment -> {
                    binding.addButton.setOnClickListener(postsClickListener)
                    binding.addButton.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.eventsFragment -> {
                    binding.addButton.setOnClickListener(eventsClickListener)
                    binding.addButton.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.usersFragment -> {
                    binding.addButton.setOnClickListener(null)
                    binding.addButton.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }
        binding.bottomMenu.setupWithNavController(navController)
        return binding.root
    }
}