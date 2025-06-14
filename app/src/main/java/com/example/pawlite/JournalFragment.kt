package com.example.pawlite

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

    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View
    private lateinit var tvLastVet: TextView

    private var lastVetVisit: String = "3 Apr 2069"
    private var weight: String = "6.9 kg"
    private var vaccines: String = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentResultListeners()
    }

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
        tvLastVet = view.findViewById(R.id.last_vet)

        // Ubah adapter untuk menangani klik
        adapter = JournalAdapter(mutableListOf()) { entry, position ->
            (activity as? MainActivity)?.navigateTo(DetailJournalFragment.newInstance(entry, position))
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        updateHealthSummaryUI()
        checkEmptyState()

        view.findViewById<Button>(R.id.btn_add_journal).setOnClickListener {
            (activity as? MainActivity)?.navigateTo(AddJournalFragment.newInstance())
        }

        view.findViewById<CardView>(R.id.card_health_summary).setOnClickListener {
            (activity as? MainActivity)?.navigateTo(
                HealthDetailFragment.newInstance(lastVetVisit, weight, vaccines)
            )
        }
    }

    private fun setupFragmentResultListeners() {
        // Listener untuk hasil dari HealthDetailFragment
        parentFragmentManager.setFragmentResultListener(HealthDetailFragment.REQUEST_KEY, this) { _, bundle ->
            lastVetVisit = bundle.getString(HealthDetailFragment.EXTRA_VET_VISIT, lastVetVisit)
            weight = bundle.getString(HealthDetailFragment.EXTRA_WEIGHT, weight)
            vaccines = bundle.getString(HealthDetailFragment.EXTRA_VACCINES, vaccines)
            updateHealthSummaryUI()
            Snackbar.make(requireView(), "Health summary updated successfully!", Snackbar.LENGTH_SHORT).show()
        }

        // Listener untuk hasil dari AddJournalFragment
        parentFragmentManager.setFragmentResultListener(AddJournalFragment.REQUEST_KEY, this) { _, bundle ->
            val newEntry = bundle.getParcelable<JournalEntry>(AddJournalFragment.RESULT_KEY)
            val isUpdate = bundle.getBoolean(AddJournalFragment.EXTRA_IS_UPDATE)
            val position = bundle.getInt(AddJournalFragment.EXTRA_POSITION)

            if (newEntry != null) {
                if (isUpdate) {
                    adapter.updateEntry(position, newEntry)
                } else {
                    adapter.addEntry(newEntry)
                }
                checkEmptyState()
            }
        }

        // Listener untuk hasil dari DetailJournalFragment
        parentFragmentManager.setFragmentResultListener(DetailJournalFragment.REQUEST_KEY, this) { _, bundle ->
            val position = bundle.getInt(DetailJournalFragment.EXTRA_POSITION, -1)
            if (position != -1 && bundle.getString("action") == DetailJournalFragment.ACTION_DELETE) {
                adapter.removeEntry(position)
                checkEmptyState()
                Snackbar.make(recyclerView, "Entri berhasil dihapus", Snackbar.LENGTH_SHORT).show()
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