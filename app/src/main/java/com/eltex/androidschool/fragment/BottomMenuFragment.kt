package com.eltex.androidschool.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eltex.androidschool.R
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eltex.androidschool.databinding.FragmentBottomMenuBinding

class BottomMenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentBottomMenuBinding.inflate(inflater, container, false)
        val eventsClickListener = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_bottomMenuFragment_to_newEventFragment)
        }

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()
        // Слушатель переключения вкладок
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.eventsFragment -> {
                    binding.addEvent.setOnClickListener(eventsClickListener)
                    binding.addEvent.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.usersFragment -> {
                    binding.addEvent.setOnClickListener(null)
                    binding.addEvent.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }
        binding.bottomMenu.setupWithNavController(navController)
        return binding.root
    }
}