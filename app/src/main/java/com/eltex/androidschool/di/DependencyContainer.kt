package com.eltex.androidschool.di

import androidx.lifecycle.ViewModelProvider

interface DependencyContainer {
   fun getPostViewModelFactory(): ViewModelProvider.Factory
   fun getNewPostViewModelFactory(id: Long): ViewModelProvider.Factory
   fun getEventViewModelFactory(): ViewModelProvider.Factory
   fun getNewEventViewModelFactory(id: Long): ViewModelProvider.Factory
}