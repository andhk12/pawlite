package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddJournalActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IS_UPDATE = "extra_is_update"
        const val EXTRA_JOURNAL_DATA = "extra_journal_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_journal)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val headerTextView = findViewById<TextView>(R.id.headerTextView)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val uploadButton = findViewById<Button>(R.id.uploadButton)

        val isUpdate = intent.getBooleanExtra(EXTRA_IS_UPDATE, false)

        if (isUpdate) {
            headerTextView.text = "UPDATE JOURNAL ENTRY"
            uploadButton.text = "Update"
            val entry = intent.getParcelableExtra<JournalEntry>(EXTRA_JOURNAL_DATA)
            if (entry != null) {
                dateEditText.setText(entry.date)
                titleEditText.setText(entry.title)
                descriptionEditText.setText(entry.description)
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        uploadButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val title = titleEditText.text.toString()
            val desc = descriptionEditText.text.toString()

            if (date.isNotBlank() && title.isNotBlank() && desc.isNotBlank()) {
                val resultIntent = Intent()
                val resultEntry = JournalEntry(date, title, desc, R.drawable.sample_cat) // default image

                if (isUpdate) {
                    resultIntent.putExtra(EXTRA_JOURNAL_DATA, resultEntry)
                } else {
                    // Logika untuk add baru
                    resultIntent.putExtra("journal_date", date)
                    resultIntent.putExtra("journal_title", title)
                    resultIntent.putExtra("journal_description", desc)
                    resultIntent.putExtra("journal_image_res", R.drawable.sample_cat)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}