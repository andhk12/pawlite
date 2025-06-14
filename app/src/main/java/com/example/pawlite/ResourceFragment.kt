package com.example.pawlite

import android.app.Activity
import android.content.Intent
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

    companion object {
        private const val ADD_RESOURCE_REQUEST_CODE = 1
        const val DETAIL_REQUEST_CODE = 5 // Gunakan kode request yang unik
    }

    private lateinit var adapter: ResourceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_resource)
        emptyState = view.findViewById(R.id.empty_state_container)

        // Kirim instance fragment ini ke adapter
        adapter = ResourceAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        view.findViewById<Button>(R.id.btn_add_resource).setOnClickListener {
            startActivityForResult(
                Intent(requireContext(), AddResourceActivity::class.java),
                ADD_RESOURCE_REQUEST_CODE
            )
        }

        checkEmptyState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                ADD_RESOURCE_REQUEST_CODE -> {
                    val tag = data.getStringExtra("resource_tag")
                    val date = data.getStringExtra("resource_date")
                    val title = data.getStringExtra("resource_title")
                    val desc = data.getStringExtra("resource_description")
                    val image = data.getIntExtra("resource_image_res", R.drawable.sample_cat)

                    if (!tag.isNullOrBlank() && !date.isNullOrBlank() && !title.isNullOrBlank() && !desc.isNullOrBlank()) {
                        val newEntry = ResourceEntry(tag, title, date, desc, image)
                        adapter.addEntry(newEntry)
                        checkEmptyState()
                    }
                }
                DETAIL_REQUEST_CODE -> {
                    val position = data.getIntExtra(DetailResourceActivity.EXTRA_POSITION, -1)
                    if (position == -1) return

                    when (data.getStringExtra("action")) {
                        DetailResourceActivity.ACTION_DELETE -> {
                            adapter.removeEntry(position)
                            checkEmptyState()
                            Snackbar.make(recyclerView, "Resource berhasil dihapus", Snackbar.LENGTH_SHORT).show()
                        }
                        DetailResourceActivity.ACTION_UPDATE -> {
                            val updatedEntry = data.getParcelableExtra<ResourceEntry>(DetailResourceActivity.EXTRA_RESOURCE_ENTRY)
                            if (updatedEntry != null) {
                                adapter.updateEntry(position, updatedEntry)
                            }
                        }
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