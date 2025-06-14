package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailJournalActivity : AppCompatActivity() {

    private var journalPosition: Int = -1

    companion object {
        const val EXTRA_JOURNAL_ENTRY = "extra_journal_entry"
        const val EXTRA_POSITION = "extra_position"
        const val ACTION_DELETE = "action_delete"
        const val ACTION_UPDATE = "action_update"
        private const val UPDATE_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_journal)

        val titleText: TextView = findViewById(R.id.titleText)
        val dateText: TextView = findViewById(R.id.dateText)
        val descriptionText: TextView = findViewById(R.id.descriptionText)
        val imageView: ImageView = findViewById(R.id.img_journal_detail)
        val updateButton: Button = findViewById(R.id.btn_update)
        val deleteButton: Button = findViewById(R.id.btn_delete)

        val entry = intent.getParcelableExtra<JournalEntry>(EXTRA_JOURNAL_ENTRY)
        journalPosition = intent.getIntExtra(EXTRA_POSITION, -1)

        if (entry != null) {
            titleText.text = entry.title
            dateText.text = entry.date
            descriptionText.text = entry.description
            imageView.setImageResource(entry.imageResId)
        }


        deleteButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(EXTRA_POSITION, journalPosition)
                putExtra("action", ACTION_DELETE)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        updateButton.setOnClickListener {
            val intent = Intent(this, AddJournalActivity::class.java).apply {
                putExtra(AddJournalActivity.EXTRA_IS_UPDATE, true)
                putExtra(AddJournalActivity.EXTRA_JOURNAL_DATA, entry)
            }
            startActivityForResult(intent, UPDATE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Forward the result from AddJournalActivity back to JournalFragment
            val updatedEntry = data?.getParcelableExtra<JournalEntry>(AddJournalActivity.EXTRA_JOURNAL_DATA)
            val resultIntent = Intent().apply {
                putExtra("action", ACTION_UPDATE)
                putExtra(EXTRA_JOURNAL_ENTRY, updatedEntry)
                putExtra(EXTRA_POSITION, journalPosition)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}