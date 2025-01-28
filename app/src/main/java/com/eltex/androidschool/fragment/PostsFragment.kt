package com.eltex.androidschool.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eltex.androidschool.R
import android.content.Intent
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.adapter.PostsAdapter
import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.databinding.FragmentPostsBinding
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.itemdecoration.PostDateDecoration
import com.eltex.androidschool.model.Post
import com.eltex.androidschool.repository.NetworkPostRepository
import com.eltex.androidschool.utils.getErrorText
import com.eltex.androidschool.viewmodel.PostViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)
        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                initializer {
                    PostViewModel(NetworkPostRepository(PostsApi.INSTANCE))
                }
            }
        }
        val adapter = PostsAdapter(
            object : PostsAdapter.PostListener {
                override fun onLikeClicked(post: Post) {
                    viewModel.like(post)
                }

                override fun onShareClicked(post: Post) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(
                            Intent.EXTRA_TEXT,
                            getString(R.string.share_text, post.author, post.content)
                        )
                        .setType("text/plain")
                    val chooser = Intent.createChooser(intent, null)
                    startActivity(chooser)
                }

                override fun onDeleteClicked(post: Post) {
                    viewModel.deleteById(post.id)
                }

                override fun onEditClicked(post: Post) {
                    findNavController().navigate(
                        R.id.action_postsFragment_to_newPostFragment,
                        bundleOf(
                            NewPostFragment.ARG_POST_ID to post.id,
                            NewPostFragment.ARG_CONTENT to post.content,
                        )
                    )
                }
            }
        )
        binding.list.adapter = adapter

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )
        binding.list.addItemDecoration(
            PostDateDecoration(
                getPost = { position ->
                    val posts = viewModel.uiState.value.posts
                    if (posts != null && position in posts.indices) {
                        viewModel.uiState.value.posts?.get(position)
                    } else {
                        null
                    }
                },
                context = this.requireContext()
            )
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.load()
        }

        binding.retryButton.setOnClickListener {
            viewModel.load()
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewPostFragment.POST_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.load()
        }

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                binding.swipeRefresh.isRefreshing = it.isRefreshing

                binding.errorGroup.isVisible = it.isEmptyError
                val errorText = it.status.throwableOrNull?.getErrorText(requireContext())
                binding.errorText.text = errorText

                binding.progress.isVisible = it.isEmptyLoading

                if (it.isRefreshError) {
                    Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
                    viewModel.consumeError()
                }

                adapter.submitList(it.posts)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}
