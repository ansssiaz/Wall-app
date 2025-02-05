package com.eltex.androidschool.feature.posts.viewmodel

import com.eltex.androidschool.mvi.Store

typealias PostStore = Store<PostUiState, PostMessage, PostEffect>