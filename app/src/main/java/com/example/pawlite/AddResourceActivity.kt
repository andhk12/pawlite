package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddResourceActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IS_UPDATE = "extra_is_update_resource"
        const val EXTRA_RESOURCE_DATA = "extra_resource_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_resource)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val headerTextView = findViewById<TextView>(R.id.headerTextView)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val uploadButton = findViewById<Button>(R.id.uploadButton)
        val tagEditText = findViewById<EditText>(R.id.tagEditText)

        val isUpdate = intent.getBooleanExtra(EXTRA_IS_UPDATE, false)

        if (isUpdate) {
            headerTextView.text = "UPDATE RESOURCE ENTRY"
            uploadButton.text = "Update"
            val entry = intent.getParcelableExtra<ResourceEntry>(EXTRA_RESOURCE_DATA)
            if (entry != null) {
                tagEditText.setText(entry.tag)
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
            val tag = tagEditText.text.toString()

            if (date.isNotBlank() && title.isNotBlank() && desc.isNotBlank() && tag.isNotBlank()) {
                val resultIntent = Intent()
                val resultEntry = ResourceEntry(tag, title, date, desc, R.drawable.sample_cat) // default image

                if (isUpdate) {
                    resultIntent.putExtra(EXTRA_RESOURCE_DATA, resultEntry)
                } else {
                    resultIntent.putExtra("resource_tag", tag)
                    resultIntent.putExtra("resource_date", date)
                    resultIntent.putExtra("resource_title", title)
                    resultIntent.putExtra("resource_description", desc)
                    resultIntent.putExtra("resource_image_res", R.drawable.sample_cat)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}