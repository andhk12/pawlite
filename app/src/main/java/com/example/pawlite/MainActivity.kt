package com.example.pawlite

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    private val journalFragment by lazy { JournalFragment() }
    private val resourceFragment by lazy { ResourceFragment() }
    private var activeFragment: Fragment = journalFragment

    private lateinit var btnJournal: Button
    private lateinit var btnResource: Button
    private lateinit var tabContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnJournal = findViewById(R.id.btn_journal)
        btnResource = findViewById(R.id.btn_resource)
        tabContainer = findViewById(R.id.tab_container)

        // Tambahkan listener untuk memantau perubahan back stack
        supportFragmentManager.addOnBackStackChangedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, resourceFragment, "resource").hide(resourceFragment)
                .add(R.id.fragment_container, journalFragment, "journal")
                .commit()
            updateButtonUI(isJournalSelected = true)
        }

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

    // Fungsi ini akan dipanggil setiap kali ada perubahan pada back stack
    override fun onBackStackChanged() {
        // Cek apakah ada fragment dalam back stack
        val hasBackStack = supportFragmentManager.backStackEntryCount > 0
        // Sembunyikan tab jika ada fragment di back stack, jika tidak, tampilkan
        tabContainer.visibility = if (hasBackStack) View.GONE else View.VISIBLE
    }

    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
        // Logika untuk menyembunyikan tab dipindahkan ke onBackStackChanged
    }

    override fun onBackPressed() {
        // Logika onBackPressed sudah ditangani oleh popBackStack() yang akan
        // memicu onBackStackChanged(), jadi kita hanya perlu memanggilnya.
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun updateButtonUI(isJournalSelected: Boolean) {
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
}