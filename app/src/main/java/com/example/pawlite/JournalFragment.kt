package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class JournalFragment : Fragment() {

    companion object {
        private const val ADD_JOURNAL_REQUEST_CODE = 1
    }

    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_journal)
        emptyState = view.findViewById(R.id.empty_state_container)

        adapter = JournalAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Swipe to delete
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedItem = adapter.getEntry(position)

                adapter.removeEntry(position)
                checkEmptyState()

                Snackbar.make(recyclerView, "Entri dihapus", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        adapter.restoreEntry(deletedItem, position)
                        checkEmptyState()
                    }.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        checkEmptyState()

        view.findViewById<Button>(R.id.btn_add_journal).setOnClickListener {
            startActivityForResult(Intent(requireContext(), AddJournalActivity::class.java), ADD_JOURNAL_REQUEST_CODE)
        }

        view.findViewById<CardView>(R.id.card_health_summary).setOnClickListener {
            val intent = Intent(requireContext(), HealthDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_JOURNAL_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
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
}