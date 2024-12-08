package com.eltex.androidschool

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.model.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var post = Post(
            id = 1L,
            content = "Слушайте, а как вы относитесь к тому, чтобы собраться большой компанией и поиграть в настолки? У меня есть несколько клевых игр, можем устроить вечер настолок! Пишите в лс или звоните",
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
        )
        bindPost(binding, post)
        binding.like.setOnClickListener {
            post = post.copy(likedByMe = !post.likedByMe)
            bindPost(binding, post)
        }
        binding.share.setOnClickListener {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show()
        }
        binding.menu.setOnClickListener {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindPost(binding: ActivityMainBinding, post: Post) {
        binding.content.text = post.content
        binding.like.setIconResource(
            if (post.likedByMe) {
                R.drawable.liked_icon
            } else {
                R.drawable.like_icon_border
            }
        )
        binding.author.text = post.author
        binding.published.text = post.published
        binding.avatarInitials.text = post.author.take(1)
        binding.like.text = if (post.likedByMe) {
            1
        } else {
            0
        }
            .toString()
    }
}