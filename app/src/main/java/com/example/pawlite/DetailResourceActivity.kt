package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailResourceActivity : AppCompatActivity() {

    private var resourcePosition: Int = -1

    companion object {
        const val EXTRA_RESOURCE_ENTRY = "extra_resource_entry"
        const val EXTRA_POSITION = "extra_position_resource"
        const val ACTION_DELETE = "action_delete_resource"
        const val ACTION_UPDATE = "action_update_resource"
        private const val UPDATE_REQUEST_CODE = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resource)

        // Inisialisasi View
        val tagText = findViewById<TextView>(R.id.tagText)
        val titleText = findViewById<TextView>(R.id.titleText)
        val dateText = findViewById<TextView>(R.id.dateText)
        val descriptionText = findViewById<TextView>(R.id.descriptionText)
        val imageView = findViewById<ImageView>(R.id.img_resource_detail)
        val updateButton = findViewById<Button>(R.id.btn_update)
        val deleteButton = findViewById<Button>(R.id.btn_delete)

        // Ambil data dari intent
        val entry = intent.getParcelableExtra<ResourceEntry>(EXTRA_RESOURCE_ENTRY)
        resourcePosition = intent.getIntExtra(EXTRA_POSITION, -1)

        // Set nilai ke tampilan
        if (entry != null) {
            tagText.text = entry.tag
            titleText.text = entry.title
            dateText.text = entry.date
            descriptionText.text = entry.description
            imageView.setImageResource(entry.imageResId)
        }


        // Tombol delete
        deleteButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(EXTRA_POSITION, resourcePosition)
                putExtra("action", ACTION_DELETE)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Tombol update
        updateButton.setOnClickListener {
            val intent = Intent(this, AddResourceActivity::class.java).apply {
                putExtra(AddResourceActivity.EXTRA_IS_UPDATE, true)
                putExtra(AddResourceActivity.EXTRA_RESOURCE_DATA, entry)
            }
            startActivityForResult(intent, UPDATE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Forward hasil dari AddResourceActivity kembali ke ResourceFragment
            val updatedEntry = data?.getParcelableExtra<ResourceEntry>(AddResourceActivity.EXTRA_RESOURCE_DATA)
            val resultIntent = Intent().apply {
                putExtra("action", ACTION_UPDATE)
                putExtra(EXTRA_RESOURCE_ENTRY, updatedEntry)
                putExtra(EXTRA_POSITION, resourcePosition)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}