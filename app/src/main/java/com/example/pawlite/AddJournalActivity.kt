package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddJournalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_journal)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val uploadButton = findViewById<Button>(R.id.uploadButton)

        backButton.setOnClickListener {
            finish()
        }

        uploadButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val title = titleEditText.text.toString()
            val desc = descriptionEditText.text.toString()

            if (date.isNotBlank() && title.isNotBlank() && desc.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra("journal_date", date)
                    putExtra("journal_title", title)
                    putExtra("journal_description", desc)
                    putExtra("journal_image_res", R.drawable.sample_cat) // default image
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
