package com.example.pawlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult

class AddResourceFragment : Fragment() {

    companion object {
        const val REQUEST_KEY = "add_resource_request"
        const val RESULT_KEY = "resource_result"

        const val EXTRA_IS_UPDATE = "extra_is_update_resource"
        const val EXTRA_RESOURCE_DATA = "extra_resource_data"
        const val EXTRA_POSITION = "extra_position_resource"

        fun newInstance(isUpdate: Boolean = false, resourceEntry: ResourceEntry? = null, position: Int = -1): AddResourceFragment {
            val fragment = AddResourceFragment()
            fragment.arguments = bundleOf(
                EXTRA_IS_UPDATE to isUpdate,
                EXTRA_RESOURCE_DATA to resourceEntry,
                EXTRA_POSITION to position
            )
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_add_resource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val headerTextView = view.findViewById<TextView>(R.id.headerTextView)
        val dateEditText = view.findViewById<EditText>(R.id.dateEditText)
        val titleEditText = view.findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = view.findViewById<EditText>(R.id.descriptionEditText)
        val uploadButton = view.findViewById<Button>(R.id.uploadButton)
        val tagEditText = view.findViewById<EditText>(R.id.tagEditText)

        val isUpdate = arguments?.getBoolean(EXTRA_IS_UPDATE, false) ?: false
        val position = arguments?.getInt(EXTRA_POSITION, -1) ?: -1

        if (isUpdate) {
            headerTextView.text = "UPDATE RESOURCE ENTRY"
            uploadButton.text = "Update"
            val entry = arguments?.getParcelable<ResourceEntry>(EXTRA_RESOURCE_DATA)
            if (entry != null) {
                tagEditText.setText(entry.tag)
                dateEditText.setText(entry.date)
                titleEditText.setText(entry.title)
                descriptionEditText.setText(entry.description)
            }
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        uploadButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val title = titleEditText.text.toString()
            val desc = descriptionEditText.text.toString()
            val tag = tagEditText.text.toString()

            if (date.isNotBlank() && title.isNotBlank() && desc.isNotBlank() && tag.isNotBlank()) {
                val resultEntry = ResourceEntry(tag, title, date, desc, R.drawable.sample_cat) // default image

                val resultBundle = bundleOf(
                    RESULT_KEY to resultEntry,
                    EXTRA_IS_UPDATE to isUpdate,
                    EXTRA_POSITION to position
                )
                setFragmentResult(REQUEST_KEY, resultBundle)
                parentFragmentManager.popBackStack()

            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}