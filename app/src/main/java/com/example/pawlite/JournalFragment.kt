package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class JournalFragment : Fragment() {

    companion object {
        private const val ADD_JOURNAL_REQUEST_CODE = 1
        const val DETAIL_REQUEST_CODE = 3
        private const val UPDATE_HEALTH_REQUEST_CODE = 4 // Kode unik untuk health update
    }

    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View

    // Deklarasikan TextView untuk Health Summary
    private lateinit var tvLastVet: TextView

    // Variabel untuk menyimpan data health summary
    private var lastVetVisit: String = "3 Apr 2069"
    private var weight: String = "6.9 kg"
    private var vaccines: String = "1"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_journal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_journal)
        emptyState = view.findViewById(R.id.empty_state_container)
        tvLastVet = view.findViewById(R.id.last_vet) // Inisialisasi TextView

        // Kirim instance fragment ini ke adapter
        adapter = JournalAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        updateHealthSummaryUI() // Panggil fungsi untuk update UI
        checkEmptyState()

        view.findViewById<Button>(R.id.btn_add_journal).setOnClickListener {
            startActivityForResult(Intent(requireContext(), AddJournalActivity::class.java), ADD_JOURNAL_REQUEST_CODE)
        }

        view.findViewById<CardView>(R.id.card_health_summary).setOnClickListener {
            val intent = Intent(requireContext(), HealthDetailActivity::class.java).apply {
                putExtra(HealthDetailActivity.EXTRA_VET_VISIT, lastVetVisit)
                putExtra(HealthDetailActivity.EXTRA_WEIGHT, weight)
                putExtra(HealthDetailActivity.EXTRA_VACCINES, vaccines)
            }
            startActivityForResult(intent, UPDATE_HEALTH_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                // Handle Add New
                ADD_JOURNAL_REQUEST_CODE -> {
                    val date = data.getStringExtra("journal_date")
                    val title = data.getStringExtra("journal_title")
                    val desc = data.getStringExtra("journal_description")
                    val image = data.getIntExtra("journal_image_res", R.drawable.sample_cat)

                    if (!date.isNullOrBlank() && !title.isNullOrBlank() && !desc.isNullOrBlank()) {
                        val newEntry = JournalEntry(date, title, desc, image)
                        adapter.addEntry(newEntry)
                        checkEmptyState()
                    }
                }
                // Handle result from Detail (Update or Delete)
                DETAIL_REQUEST_CODE -> {
                    val position = data.getIntExtra(DetailJournalActivity.EXTRA_POSITION, -1)
                    if (position == -1) return

                    when (data.getStringExtra("action")) {
                        DetailJournalActivity.ACTION_DELETE -> {
                            adapter.removeEntry(position)
                            checkEmptyState()
                            Snackbar.make(recyclerView, "Entri berhasil dihapus", Snackbar.LENGTH_SHORT).show()
                        }
                        DetailJournalActivity.ACTION_UPDATE -> {
                            val updatedEntry = data.getParcelableExtra<JournalEntry>(DetailJournalActivity.EXTRA_JOURNAL_ENTRY)
                            if (updatedEntry != null) {
                                adapter.updateEntry(position, updatedEntry)
                            }
                        }
                    }
                }
                // Handle result from HealthDetailActivity
                UPDATE_HEALTH_REQUEST_CODE -> {
                    lastVetVisit = data.getStringExtra(HealthDetailActivity.EXTRA_VET_VISIT) ?: lastVetVisit
                    weight = data.getStringExtra(HealthDetailActivity.EXTRA_WEIGHT) ?: weight
                    vaccines = data.getStringExtra(HealthDetailActivity.EXTRA_VACCINES) ?: vaccines
                    updateHealthSummaryUI()
                    Snackbar.make(requireView(), "Health summary updated successfully!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkEmptyState() {
        if (adapter.itemCount == 0) {
            recyclerView.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
    }

    private fun updateHealthSummaryUI() {
        tvLastVet.text = "Last Vet Visit: $lastVetVisit\nWeight: $weight\nVaccines: $vaccines"
    }
}