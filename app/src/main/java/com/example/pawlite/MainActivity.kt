package com.example.pawlite

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    // 1. Buat instance untuk setiap fragment dan simpan.
    // Ini memastikan kita selalu menggunakan objek yang sama dan tidak membuat yang baru.
    private val journalFragment by lazy { JournalFragment() }
    private val resourceFragment by lazy { ResourceFragment() }
    private var activeFragment: Fragment = journalFragment // Lacak fragment yang sedang aktif

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnJournal: Button = findViewById(R.id.btn_journal)
        val btnResource: Button = findViewById(R.id.btn_resource)

        // 2. Saat aplikasi pertama kali dijalankan, tambahkan kedua fragment ke FragmentManager.
        // Atur JournalFragment sebagai yang terlihat (show) dan sembunyikan (hide) ResourceFragment.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, resourceFragment, "resource").hide(resourceFragment)
                .add(R.id.fragment_container, journalFragment, "journal")
                .commit()
            updateButtonUI(isJournalSelected = true) // Set tampilan tombol awal
        }

        // 3. Ubah listener klik untuk menyembunyikan fragment aktif dan menampilkan yang baru.
        // Ini tidak akan menghancurkan state fragment.
        btnJournal.setOnClickListener {
            if (activeFragment != journalFragment) {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(journalFragment).commit()
                activeFragment = journalFragment
                updateButtonUI(isJournalSelected = true)
            }
        }

        btnResource.setOnClickListener {
            if (activeFragment != resourceFragment) {
                supportFragmentManager.beginTransaction().hide(activeFragment).show(resourceFragment).commit()
                activeFragment = resourceFragment
                updateButtonUI(isJournalSelected = false)
            }
        }
    }

    // Helper function untuk memperbarui tampilan tombol
    private fun updateButtonUI(isJournalSelected: Boolean) {
        val btnJournal: Button = findViewById(R.id.btn_journal)
        val btnResource: Button = findViewById(R.id.btn_resource)

        if (isJournalSelected) {
            btnJournal.setBackgroundResource(R.drawable.tab_selected)
            btnJournal.setTextColor(getColor(android.R.color.white))
            btnResource.setBackgroundResource(R.drawable.tab_unselected)
            btnResource.setTextColor(getColor(android.R.color.black))
        } else {
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