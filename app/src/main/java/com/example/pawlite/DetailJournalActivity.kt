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

class DetailJournalFragment : Fragment() {

    private var journalPosition: Int = -1
    private var journalEntry: JournalEntry? = null

    companion object {
        const val REQUEST_KEY = "detail_journal_request"
        const val ACTION_DELETE = "action_delete"
        const val EXTRA_JOURNAL_ENTRY = "extra_journal_entry"
        const val EXTRA_POSITION = "extra_position"

        fun newInstance(entry: JournalEntry, position: Int): DetailJournalFragment {
            val fragment = DetailJournalFragment()
            fragment.arguments = bundleOf(
                EXTRA_JOURNAL_ENTRY to entry,
                EXTRA_POSITION to position
            )
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_detail_journal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleText: TextView = view.findViewById(R.id.titleText)
        val dateText: TextView = view.findViewById(R.id.dateText)
        val descriptionText: TextView = view.findViewById(R.id.descriptionText)
        val imageView: ImageView = view.findViewById(R.id.img_journal_detail)
        val updateButton: Button = view.findViewById(R.id.btn_update)
        val deleteButton: Button = view.findViewById(R.id.btn_delete)

        journalEntry = arguments?.getParcelable(EXTRA_JOURNAL_ENTRY)
        journalPosition = arguments?.getInt(EXTRA_POSITION, -1) ?: -1


        journalEntry?.let { entry ->
            titleText.text = entry.title
            dateText.text = entry.date
            descriptionText.text = entry.description
            imageView.setImageResource(entry.imageResId)
        }

        deleteButton.setOnClickListener {
            val resultBundle = bundleOf(
                "action" to ACTION_DELETE,
                EXTRA_POSITION to journalPosition
            )
            setFragmentResult(REQUEST_KEY, resultBundle)
            parentFragmentManager.popBackStack()
        }

        updateButton.setOnClickListener {
            journalEntry?.let { entry ->
                (activity as? MainActivity)?.navigateTo(
                    AddJournalFragment.newInstance(isUpdate = true, journalEntry = entry, position = journalPosition)
                )
            }
        }
    }
}