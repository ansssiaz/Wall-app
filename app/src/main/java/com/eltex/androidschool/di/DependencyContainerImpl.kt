package com.eltex.androidschool.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.api.OkHttpClientFactory
import com.eltex.androidschool.api.RetrofitFactory
import com.eltex.androidschool.feature.events.api.EventsApi
import com.eltex.androidschool.feature.events.data.di.EventStoreFactory
import com.eltex.androidschool.feature.events.repository.NetworkEventRepository
import com.eltex.androidschool.feature.events.viewmodel.EventViewModel
import com.eltex.androidschool.feature.newevent.viewmodel.NewEventViewModel
import com.eltex.androidschool.feature.newpost.viewmodel.NewPostViewModel
import com.eltex.androidschool.feature.posts.api.PostsApi
import com.eltex.androidschool.feature.posts.data.di.PostStoreFactory
import com.eltex.androidschool.feature.posts.repository.NetworkPostRepository
import com.eltex.androidschool.feature.posts.viewmodel.PostViewModel
import retrofit2.create

class DependencyContainerImpl: DependencyContainer {
    private val okHttpClient = OkHttpClientFactory.createOkHttpClient()

    private val retrofit = RetrofitFactory.createRetrofit(okHttpClient = okHttpClient)

    private val postsApi: PostsApi = retrofit.create()

    private val postRepository: NetworkPostRepository = NetworkPostRepository(api = postsApi)

    private val postStoreFactory : PostStoreFactory = PostStoreFactory(repository = postRepository)

    private val eventsApi: EventsApi = retrofit.create()

    private val eventRepository: NetworkEventRepository = NetworkEventRepository(api = eventsApi)

    private val eventStoreFactory : EventStoreFactory = EventStoreFactory(repository = eventRepository)

    override fun getPostViewModelFactory(): ViewModelProvider.Factory = viewModelFactory{
        addInitializer(PostViewModel::class){
            PostViewModel(postStoreFactory.create())
        }
    }

    override fun getNewPostViewModelFactory(id: Long): ViewModelProvider.Factory = viewModelFactory{
        addInitializer(NewPostViewModel::class){
            NewPostViewModel(postRepository, id)
        }
    }

    override fun getEventViewModelFactory(): ViewModelProvider.Factory = viewModelFactory{
        addInitializer(EventViewModel::class){
            EventViewModel(eventStoreFactory.create())
        }
    }

    override fun getNewEventViewModelFactory(id: Long): ViewModelProvider.Factory = viewModelFactory{
        addInitializer(NewEventViewModel::class){
            NewEventViewModel(eventRepository, id)
        }
    }
}