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

class ResourceFragment : Fragment() {

    companion object {
        private const val ADD_RESOURCE_REQUEST_CODE = 1
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

        adapter = ResourceAdapter(mutableListOf()) // Awal kosong
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

        if (requestCode == ADD_RESOURCE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
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