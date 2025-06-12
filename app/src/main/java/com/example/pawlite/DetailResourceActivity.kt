package com.example.pawlite

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailResourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resource)

        // Ambil data dari intent
        val tag = intent.getStringExtra("tag")
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val description = intent.getStringExtra("description")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.sample_cat)

        // Inisialisasi View
        val tagText = findViewById<TextView>(R.id.tag)
        val titleText = findViewById<TextView>(R.id.titleText)
        val dateText = findViewById<TextView>(R.id.dateText)
        val descriptionText = findViewById<TextView>(R.id.descriptionText)
        val imageView = findViewById<ImageView>(R.id.img_cat)
        val backButton = findViewById<ImageView>(R.id.backButton)
        val deleteButton = findViewById<Button>(R.id.btn_delete)

        // Set nilai ke tampilan
        tagText.text = tag
        titleText.text = title
        dateText.text = date
        descriptionText.text = description
        imageView.setImageResource(imageResId)

        // Tombol kembali
        backButton.setOnClickListener {
            finish()
        }

        // Tombol delete (sementara hanya finish)
        deleteButton.setOnClickListener {

            finish()
        }
    }
}
