package com.example.pawlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(private val journalEntries: MutableList<JournalEntry>) :
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
    }

    override fun getItemCount() = journalEntries.size

    fun addEntry(entry: JournalEntry) {
        journalEntries.add(0, entry) // Menambahkan di posisi paling atas
        notifyItemInserted(0)
    }

    fun removeEntry(position: Int) {
        journalEntries.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getEntry(position: Int): JournalEntry {
        return journalEntries[position]
    }

    fun restoreEntry(entry: JournalEntry, position: Int) {
        journalEntries.add(position, entry)
        notifyItemInserted(position)
    }
}