package com.example.pawlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ResourceFragment : Fragment() {

    private lateinit var adapter: ResourceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View

    private lateinit var btnAll: Button
    private lateinit var btnNutrition: Button
    private lateinit var btnGrooming: Button
    private lateinit var btnHealth: Button
    private lateinit var filterButtons: List<Button>

    private val masterResourceEntries = mutableListOf<ResourceEntry>()
    private val displayedResourceEntries = mutableListOf<ResourceEntry>()

    private var currentFilterTag: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // --- PERUBAHAN DI SINI: Hapus pemanggilan getInitialDummyData ---
        // if (masterResourceEntries.isEmpty()) {
        //     masterResourceEntries.addAll(getInitialDummyData())
        // }

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

        adapter = ResourceAdapter(displayedResourceEntries) { entry, _ ->
            val masterIndex = masterResourceEntries.indexOf(entry)
            if (masterIndex != -1) {
                (activity as? MainActivity)?.navigateTo(DetailResourceFragment.newInstance(entry, masterIndex))
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        view.findViewById<Button>(R.id.btn_add_resource).setOnClickListener {
            (activity as? MainActivity)?.navigateTo(AddResourceFragment.newInstance())
        }

        setupFilterButtons(view)
        applyFilter()
    }

    private fun setupFilterButtons(view: View) {
        btnAll = view.findViewById(R.id.btn_filter_all)
        btnNutrition = view.findViewById(R.id.btn_filter_nutrition)
        btnGrooming = view.findViewById(R.id.btn_filter_grooming)
        btnHealth = view.findViewById(R.id.btn_filter_health)
        filterButtons = listOf(btnAll, btnNutrition, btnGrooming, btnHealth)

        btnAll.setOnClickListener {
            currentFilterTag = "All"
            applyFilter()
        }
        btnNutrition.setOnClickListener {
            currentFilterTag = "#Nutrition"
            applyFilter()
        }
        btnGrooming.setOnClickListener {
            currentFilterTag = "#Grooming"
            applyFilter()
        }
        btnHealth.setOnClickListener {
            currentFilterTag = "#Health"
            applyFilter()
        }
    }

    private fun applyFilter() {
        val filteredList = if (currentFilterTag == "All") {
            masterResourceEntries
        } else {
            masterResourceEntries.filter { it.tag.equals(currentFilterTag, ignoreCase = true) }
        }

        displayedResourceEntries.clear()
        displayedResourceEntries.addAll(filteredList)
        adapter.notifyDataSetChanged()
        updateButtonUI()
        checkEmptyState()
    }

    private fun updateButtonUI() {
        val selectedButton = when (currentFilterTag) {
            "#Nutrition" -> btnNutrition
            "#Grooming" -> btnGrooming
            "#Health" -> btnHealth
            else -> btnAll
        }

        filterButtons.forEach { button ->
            if (button == selectedButton) {
                button.setBackgroundResource(R.drawable.tab_selected)
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            } else {
                button.setBackgroundResource(R.drawable.tab_unselected)
                button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            }
        }
    }

    private fun setupFragmentResultListeners() {
        parentFragmentManager.setFragmentResultListener(AddResourceFragment.REQUEST_KEY, this) { _, bundle ->
            val newEntry = bundle.getParcelable<ResourceEntry>(AddResourceFragment.RESULT_KEY)
            val isUpdate = bundle.getBoolean(AddResourceFragment.EXTRA_IS_UPDATE, false)
            val position = bundle.getInt(AddResourceFragment.EXTRA_POSITION, -1)

            if (newEntry != null) {
                if (isUpdate && position != -1 && position < masterResourceEntries.size) {
                    masterResourceEntries[position] = newEntry
                } else {
                    masterResourceEntries.add(0, newEntry)
                }
                applyFilter()
            }
        }

        parentFragmentManager.setFragmentResultListener(DetailResourceFragment.REQUEST_KEY, this) { _, bundle ->
            val position = bundle.getInt(DetailResourceFragment.EXTRA_POSITION, -1)
            if (position == -1 || position >= masterResourceEntries.size) return@setFragmentResultListener

            when (bundle.getString("action")) {
                DetailResourceFragment.ACTION_DELETE -> {
                    masterResourceEntries.removeAt(position)
                    applyFilter()
                    Snackbar.make(recyclerView, "Resource berhasil dihapus", Snackbar.LENGTH_SHORT).show()
                }
                DetailResourceFragment.ACTION_UPDATE -> {
                    val entryToUpdate = bundle.getParcelable<ResourceEntry>(DetailResourceFragment.EXTRA_RESOURCE_ENTRY)
                    if (entryToUpdate != null) {
                        (activity as? MainActivity)?.navigateTo(
                            AddResourceFragment.newInstance(isUpdate = true, resourceEntry = entryToUpdate, position = position)
                        )
                    }
                }
            }
        }
    }

    private fun checkEmptyState() {
        if (displayedResourceEntries.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
    }

    // --- PERUBAHAN DI SINI: HAPUS SELURUH FUNGSI INI ---
    // private fun getInitialDummyData(): List<ResourceEntry> {
    //     ...
    // }
}