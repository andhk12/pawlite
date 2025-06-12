package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddResourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_resource)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val uploadButton = findViewById<Button>(R.id.uploadButton)
        val tagEditText = findViewById<EditText>(R.id.tagEditText)

        backButton.setOnClickListener {
            finish()
        }

        uploadButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val title = titleEditText.text.toString()
            val desc = descriptionEditText.text.toString()
            val tag = tagEditText.text.toString()

            if (date.isNotBlank() && title.isNotBlank() && desc.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra("resource_date", date)
                    putExtra("resource_title", title)
                    putExtra("resource_description", desc)
                    putExtra("resource_image_res", R.drawable.sample_cat)
                    putExtra("resource_tag", tag)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}