package com.ansssiaz.wallapp.feature.posts.repository

import com.ansssiaz.wallapp.feature.posts.data.Post

interface PostRepository {
    suspend fun like(id : Long): Post
    suspend fun deleteLike(id: Long): Post
    suspend fun savePost(id: Long, content: String): Post
    suspend fun delete(id: Long)
    suspend fun getLatest(count: Int): List<Post>
    suspend fun getBefore(id: Long, count: Int): List<Post>
}