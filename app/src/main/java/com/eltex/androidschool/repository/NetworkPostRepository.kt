package com.eltex.androidschool.repository

import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Post
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class NetworkPostRepository(private val api: PostsApi) : PostRepository {

    override fun getPosts(): Single<List<Post>> = api.getPosts()

    override fun savePost(id: Long, content: String): Single<Post> {
        return api.save(
            Post(
                id = id,
                content = content,
            )
        )
    }

    override fun delete(id: Long): Completable = api.delete(id)

    override fun like(id: Long): Single<Post> = api.like(id)

    override fun deleteLike(id: Long): Single<Post> = api.deleteLike(id)
}