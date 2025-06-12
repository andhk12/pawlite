package com.example.pawlite

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnJournal: Button = findViewById(R.id.btn_journal)
        val btnResource: Button = findViewById(R.id.btn_resource)

        // Load the initial fragment
        if (savedInstanceState == null) {
            loadFragment(JournalFragment())
            btnJournal.setBackgroundResource(R.drawable.tab_selected)
            btnJournal.setTextColor(getColor(android.R.color.white))
            btnResource.setBackgroundResource(R.drawable.tab_unselected)
            btnResource.setTextColor(getColor(android.R.color.black))
        }

        btnJournal.setOnClickListener {
            loadFragment(JournalFragment())
            // Update button UI
            btnJournal.setBackgroundResource(R.drawable.tab_selected)
            btnJournal.setTextColor(getColor(android.R.color.white))
            btnResource.setBackgroundResource(R.drawable.tab_unselected)
            btnResource.setTextColor(getColor(android.R.color.black))
        }

        btnResource.setOnClickListener {
            loadFragment(ResourceFragment())
            // Update button UI
            btnResource.setBackgroundResource(R.drawable.tab_selected)
            btnResource.setTextColor(getColor(android.R.color.white))
            btnJournal.setBackgroundResource(R.drawable.tab_unselected)
            btnJournal.setTextColor(getColor(android.R.color.black))
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}