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

    // Tombol-tombol untuk filter
    private lateinit var btnAll: Button
    private lateinit var btnNutrition: Button
    private lateinit var btnGrooming: Button
    private lateinit var btnHealth: Button
    private lateinit var filterButtons: List<Button>

    // Daftar master yang berisi semua data, tidak akan diubah oleh filter
    private val masterResourceEntries = mutableListOf<ResourceEntry>()
    // Daftar yang ditampilkan di adapter, isinya sesuai hasil filter
    private val displayedResourceEntries = mutableListOf<ResourceEntry>()

    // Untuk menyimpan state filter yang sedang aktif
    private var currentFilterTag: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menambahkan data awal ke daftar master (hanya sekali saat fragment dibuat)
        // Dalam aplikasi nyata, data ini bisa berasal dari database atau API
        if (masterResourceEntries.isEmpty()) {
            masterResourceEntries.addAll(getInitialDummyData())
        }

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

        // Inisialisasi adapter dengan daftar yang akan ditampilkan
        adapter = ResourceAdapter(displayedResourceEntries) { entry, _ ->
            // Saat item diklik, cari posisinya di daftar master untuk memastikan integritas data
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

        // Menyiapkan tombol filter
        setupFilterButtons(view)

        // Menerapkan filter saat tampilan pertama kali dibuat
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
        // 1. Buat daftar sementara hasil filter dari daftar master
        val filteredList = if (currentFilterTag == "All") {
            masterResourceEntries
        } else {
            masterResourceEntries.filter { it.tag.equals(currentFilterTag, ignoreCase = true) }
        }

        // 2. Perbarui daftar yang akan ditampilkan
        displayedResourceEntries.clear()
        displayedResourceEntries.addAll(filteredList)

        // 3. Beri tahu adapter bahwa data telah berubah
        adapter.notifyDataSetChanged()

        // 4. Perbarui tampilan tombol filter dan empty state
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
                if (isUpdate && position != -1) {
                    // Jika update, ubah data di daftar master
                    masterResourceEntries[position] = newEntry
                } else {
                    // Jika data baru, tambahkan ke daftar master
                    masterResourceEntries.add(0, newEntry)
                }
                // Terapkan kembali filter untuk memperbarui tampilan
                applyFilter()
            }
        }

        parentFragmentManager.setFragmentResultListener(DetailResourceFragment.REQUEST_KEY, this) { _, bundle ->
            val position = bundle.getInt(DetailResourceFragment.EXTRA_POSITION, -1)
            if (position == -1) return@setFragmentResultListener

            when (bundle.getString("action")) {
                DetailResourceFragment.ACTION_DELETE -> {
                    // Hapus data dari daftar master
                    masterResourceEntries.removeAt(position)
                    // Terapkan kembali filter untuk memperbarui tampilan
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

    // Contoh data awal untuk demonstrasi
    private fun getInitialDummyData(): List<ResourceEntry> {
        return listOf(
            ResourceEntry("#Grooming", "Panduan Grooming Kucing Sendiri", "12 April 2025", "Tutor dek cara mandiin kucing di rumah...", R.drawable.sample_cat),
            ResourceEntry("#Health", "Tanda-tanda Kucing Sakit", "10 April 2025", "Kucing yang sehat terlihat aktif dan waspada...", R.drawable.sample_cat),
            ResourceEntry("#Nutrition", "Makanan Terbaik untuk Anak Kucing", "8 April 2025", "Anak kucing membutuhkan nutrisi yang berbeda...", R.drawable.sample_cat),
            ResourceEntry("#Grooming", "Cara Memotong Kuku Kucing", "5 April 2025", "Memotong kuku kucing bisa jadi tantangan...", R.drawable.sample_cat)
        )
    }
}