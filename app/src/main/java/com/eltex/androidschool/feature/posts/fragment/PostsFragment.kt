package com.eltex.androidschool.feature.posts.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentPostsBinding
import com.eltex.androidschool.feature.newpost.fragment.NewPostFragment
import com.eltex.androidschool.feature.posts.adapter.PostsAdapter
import com.eltex.androidschool.feature.posts.api.PostsApi
import com.eltex.androidschool.feature.posts.effecthandler.PostEffectHandler
import com.eltex.androidschool.feature.posts.reducer.PostReducer
import com.eltex.androidschool.feature.posts.repository.NetworkPostRepository
import com.eltex.androidschool.feature.posts.ui.PostUiModel
import com.eltex.androidschool.feature.posts.ui.PostUiModelMapper
import com.eltex.androidschool.feature.posts.viewmodel.PostMessage
import com.eltex.androidschool.feature.posts.viewmodel.PostStatus.*
import com.eltex.androidschool.feature.posts.viewmodel.PostStore
import com.eltex.androidschool.feature.posts.viewmodel.PostUiState
import com.eltex.androidschool.feature.posts.viewmodel.PostViewModel
import com.eltex.androidschool.itemdecoration.OffsetDecoration
import com.eltex.androidschool.itemdecoration.PostDateDecoration
import com.eltex.androidschool.utils.getErrorText
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostsFragment : Fragment() {
    private var isLoading = false
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
                    PostViewModel(
                        PostStore(
                            PostReducer(),
                            PostEffectHandler(
                                NetworkPostRepository(PostsApi.INSTANCE),
                                PostUiModelMapper()
                            ),
                            setOf(PostMessage.Refresh),
                            PostUiState(),
                        )
                    )
                }
            }
        }
        val adapter = PostsAdapter(
            object : PostsAdapter.PostListener {
                override fun onLikeClicked(post: PostUiModel) {
                    viewModel.accept(PostMessage.Like(post))
                }

                override fun onShareClicked(post: PostUiModel) {
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

                override fun onDeleteClicked(post: PostUiModel) {
                    viewModel.accept(PostMessage.Delete(post))
                }

                override fun onEditClicked(post: PostUiModel) {
                    requireParentFragment().requireParentFragment().findNavController().navigate(
                        R.id.action_bottomMenuFragment_to_newPostFragment,
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
                    if (position in posts.indices) {
                        viewModel.uiState.value.posts[position]
                    } else {
                        null
                    }
                },
                context = this.requireContext()
            )
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(PostMessage.Refresh)
        }

        binding.retryButton.setOnClickListener {
            viewModel.accept(PostMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewPostFragment.POST_CREATED_RESULT,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(PostMessage.Refresh)
        }

        binding.list.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    val itemsCount = adapter.itemCount
                    val adapterPosition = binding.list.getChildAdapterPosition(view)

                    //Если пользователь доскроллил до середины списка, и данные сейчас не загружаются - загружаем следующую страницу
                    if (adapterPosition >= itemsCount / 2 && adapterPosition < itemsCount - 2 && !isLoading) {
                        isLoading = true
                        viewModel.accept(PostMessage.LoadNextPage)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) = Unit
            }
        )

        viewModel.uiState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                isLoading = when (it.status) {
                    NextPageLoading -> {
                        true
                    }

                    Idle, EmptyLoading, Refreshing, is EmptyError, is NextPageError -> {
                        false
                    }
                }
                binding.swipeRefresh.isRefreshing = it.isRefreshing

                binding.errorGroup.isVisible = it.isEmptyError
                val errorText = it.emptyError?.getErrorText(requireContext())
                binding.errorText.text = errorText

                binding.progress.isVisible = it.isEmptyLoading

                if (it.singleError != null) {
                    val singleErrorText = it.singleError.getErrorText(requireContext())
                    Toast.makeText(requireContext(), singleErrorText, Toast.LENGTH_SHORT).show()
                    viewModel.accept(PostMessage.HandleError)
                }

                adapter.submitList(it.posts)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }
}
