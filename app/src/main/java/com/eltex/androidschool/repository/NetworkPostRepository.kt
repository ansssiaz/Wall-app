package com.eltex.androidschool.repository

import com.eltex.androidschool.api.PostsApi
import com.eltex.androidschool.model.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.threeten.bp.Instant

class NetworkPostRepository(private val api: PostsApi) : PostRepository {

    override fun getPosts(callback: com.eltex.androidschool.utils.Callback<List<Post>>) {
        api.getPosts().enqueue(
            object : Callback<List<Post>> {
                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code is ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun savePost(
        id: Long,
        content: String,
        callback: com.eltex.androidschool.utils.Callback<Post>
    ) {
        val today = Instant.now().toString()

        api.save(
            Post(
                id = id,
                content = content,
                published = today,
            )
        ).enqueue(
            object : Callback<Post> {
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun delete(id: Long, callback: com.eltex.androidschool.utils.Callback<Unit>) {
        api.delete(id).enqueue(
            object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        callback.onSuccess(Unit)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun like(id: Long, callback: com.eltex.androidschool.utils.Callback<Post>) {
        api.like(id).enqueue(
            object : Callback<Post> {
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }

    override fun deleteLike(id: Long, callback: com.eltex.androidschool.utils.Callback<Post>) {
        api.deleteLike(id).enqueue(
            object : Callback<Post> {
                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            callback.onError(RuntimeException("Response body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    } else {
                        callback.onError(RuntimeException("Response code: ${response.code()}"))
                    }
                }
            }
        )
    }
}