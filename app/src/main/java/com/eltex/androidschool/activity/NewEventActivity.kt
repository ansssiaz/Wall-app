package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityNewEventBinding

class NewEventActivity : AppCompatActivity() {
    private var eventId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventId = intent.getLongExtra("EXTRA_EVENT_ID", -1L)
        val initialContent = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        binding.content.setText(initialContent)

        val title =
            if (eventId != null && eventId != -1L) R.string.edit_event_title else R.string.new_event_title
        binding.toolbar.title = getString(title)

        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
            val content = binding.content.text?.toString().orEmpty()
            if (content.isBlank()) {
                Toast.makeText(this, R.string.empty_event_error, Toast.LENGTH_SHORT).show()
            } else {
                val resultIntent = Intent().putExtra(Intent.EXTRA_TEXT, content)
                if (eventId != null) {
                    resultIntent.putExtra("EXTRA_EVENT_ID", eventId)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
            true
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}