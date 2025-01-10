package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityNewEventBinding

class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
            val content = binding.content.text?.toString().orEmpty()
            if (content.isBlank()) {
                Toast.makeText(this, R.string.empty_event_error, Toast.LENGTH_SHORT).show()
            } else {
                setResult(RESULT_OK, Intent().putExtra(Intent.EXTRA_TEXT, content))
                finish()
            }
            true
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}