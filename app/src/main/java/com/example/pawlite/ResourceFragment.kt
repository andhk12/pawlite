package com.example.pawlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ResourceFragment : Fragment() {

    private lateinit var adapter: ResourceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View

    // 1. Pindahkan list data ke sini agar tidak direset saat view dibuat ulang
    private val resourceEntries = mutableListOf<ResourceEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentResultListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_resource)
        emptyState = view.findViewById(R.id.empty_state_container)

        // 2. Gunakan list milik fragment saat inisialisasi adapter
        adapter = ResourceAdapter(resourceEntries) { entry, position ->
            (activity as? MainActivity)?.navigateTo(DetailResourceFragment.newInstance(entry, position))
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        view.findViewById<Button>(R.id.btn_add_resource).setOnClickListener {
            (activity as? MainActivity)?.navigateTo(AddResourceFragment.newInstance())
        }

        checkEmptyState()
    }

    private fun setupFragmentResultListeners() {
        parentFragmentManager.setFragmentResultListener(AddResourceFragment.REQUEST_KEY, this) { _, bundle ->
            val newEntry = bundle.getParcelable<ResourceEntry>(AddResourceFragment.RESULT_KEY)
            // Menambahkan default value untuk keamanan
            val isUpdate = bundle.getBoolean(AddResourceFragment.EXTRA_IS_UPDATE, false)
            val position = bundle.getInt(AddResourceFragment.EXTRA_POSITION, -1)

            if (newEntry != null) {
                if (isUpdate && position != -1) {
                    adapter.updateEntry(position, newEntry)
                    Snackbar.make(requireView(), "Resource updated successfully!", Snackbar.LENGTH_SHORT).show()
                } else {
                    adapter.addEntry(newEntry)
                }
                checkEmptyState()
            }
        }

        parentFragmentManager.setFragmentResultListener(DetailResourceFragment.REQUEST_KEY, this) { _, bundle ->
            val position = bundle.getInt(DetailResourceFragment.EXTRA_POSITION, -1)
            when (bundle.getString("action")) {
                DetailResourceFragment.ACTION_DELETE -> {
                    if (position != -1) {
                        adapter.removeEntry(position)
                        checkEmptyState()
                        Snackbar.make(recyclerView, "Resource berhasil dihapus", Snackbar.LENGTH_SHORT).show()
                    }
                }
                DetailResourceFragment.ACTION_UPDATE -> {
                    val entryToUpdate = bundle.getParcelable<ResourceEntry>(DetailResourceFragment.EXTRA_RESOURCE_ENTRY)
                    if (position != -1 && entryToUpdate != null) {
                        (activity as? MainActivity)?.navigateTo(
                            AddResourceFragment.newInstance(isUpdate = true, resourceEntry = entryToUpdate, position = position)
                        )
                    }
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
}