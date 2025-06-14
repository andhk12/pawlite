package com.example.pawlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult

class DetailResourceFragment : Fragment() {

    private var resourcePosition: Int = -1
    private var resourceEntry: ResourceEntry? = null

    companion object {
        const val REQUEST_KEY = "detail_resource_request"
        const val ACTION_DELETE = "action_delete_resource"
        const val EXTRA_RESOURCE_ENTRY = "extra_resource_entry"
        const val EXTRA_POSITION = "extra_position_resource"

        fun newInstance(entry: ResourceEntry, position: Int): DetailResourceFragment {
            val fragment = DetailResourceFragment()
            fragment.arguments = bundleOf(
                EXTRA_RESOURCE_ENTRY to entry,
                EXTRA_POSITION to position
            )
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_detail_resource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tagText = view.findViewById<TextView>(R.id.tagText)
        val titleText = view.findViewById<TextView>(R.id.titleText)
        val dateText = view.findViewById<TextView>(R.id.dateText)
        val descriptionText = view.findViewById<TextView>(R.id.descriptionText)
        val imageView = view.findViewById<ImageView>(R.id.img_resource_detail)
        val updateButton = view.findViewById<Button>(R.id.btn_update)
        val deleteButton = view.findViewById<Button>(R.id.btn_delete)

        resourceEntry = arguments?.getParcelable(EXTRA_RESOURCE_ENTRY)
        resourcePosition = arguments?.getInt(EXTRA_POSITION, -1) ?: -1

        resourceEntry?.let { entry ->
            tagText.text = entry.tag
            titleText.text = entry.title
            dateText.text = entry.date
            descriptionText.text = entry.description
            imageView.setImageResource(entry.imageResId)
        }

        deleteButton.setOnClickListener {
            val resultBundle = bundleOf(
                "action" to ACTION_DELETE,
                EXTRA_POSITION to resourcePosition
            )
            setFragmentResult(REQUEST_KEY, resultBundle)
            parentFragmentManager.popBackStack()
        }

        updateButton.setOnClickListener {
            resourceEntry?.let { entry ->
                (activity as? MainActivity)?.navigateTo(
                    AddResourceFragment.newInstance(isUpdate = true, resourceEntry = entry, position = resourcePosition)
                )
            }
        }
    }
}