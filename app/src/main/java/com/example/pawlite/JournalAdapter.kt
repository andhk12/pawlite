package com.example.pawlite

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(private val journalEntries: MutableList<JournalEntry>, private val fragment: JournalFragment) :
    RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val journalImage: ImageView = itemView.findViewById(R.id.journal_image)
        val journalDate: TextView = itemView.findViewById(R.id.journal_date)
        val journalTitle: TextView = itemView.findViewById(R.id.journal_title)
        val journalDescription: TextView = itemView.findViewById(R.id.journal_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_journal_entry, parent, false)
        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val entry = journalEntries[position]
        holder.journalImage.setImageResource(entry.imageResId)
        holder.journalDate.text = entry.date
        holder.journalTitle.text = entry.title
        holder.journalDescription.text = entry.description

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailJournalActivity::class.java).apply {
                putExtra(DetailJournalActivity.EXTRA_JOURNAL_ENTRY, entry)
                putExtra(DetailJournalActivity.EXTRA_POSITION, position)
            }
            // Gunakan fragment untuk memulai activity for result
            fragment.startActivityForResult(intent, JournalFragment.DETAIL_REQUEST_CODE)
        }
    }

    override fun getItemCount() = journalEntries.size

    fun addEntry(entry: JournalEntry) {
        journalEntries.add(0, entry)
        notifyItemInserted(0)
    }

    fun updateEntry(position: Int, entry: JournalEntry) {
        if (position >= 0 && position < journalEntries.size) {
            journalEntries[position] = entry
            notifyItemChanged(position)
        }
    }

    fun removeEntry(position: Int) {
        if (position >= 0 && position < journalEntries.size) {
            journalEntries.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getEntry(position: Int): JournalEntry {
        return journalEntries[position]
    }

    fun restoreEntry(entry: JournalEntry, position: Int) {
        journalEntries.add(position, entry)
        notifyItemInserted(position)
    }
}