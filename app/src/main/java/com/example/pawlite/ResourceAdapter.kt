package com.example.pawlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResourceAdapter(
    private val resources: MutableList<ResourceEntry>,
    private val onItemClicked: (ResourceEntry, Int) -> Unit
) : RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.findViewById(R.id.text_tag)
        val title: TextView = itemView.findViewById(R.id.text_title)
        val date: TextView = itemView.findViewById(R.id.text_date)
        val description: TextView = itemView.findViewById(R.id.text_description)
        val image: ImageView = itemView.findViewById(R.id.image_resource)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resource_entry, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val resource = resources[position]
        holder.tag.text = resource.tag
        holder.title.text = resource.title
        holder.date.text = resource.date
        holder.description.text = resource.description
        holder.image.setImageResource(resource.imageResId)

        holder.itemView.setOnClickListener {
            onItemClicked(resource, position)
        }
    }

    override fun getItemCount(): Int = resources.size

    fun addEntry(entry: ResourceEntry) {
        resources.add(0, entry)
        notifyItemInserted(0)
    }

    fun updateEntry(position: Int, entry: ResourceEntry) {
        if (position >= 0 && position < resources.size) {
            resources[position] = entry
            notifyItemChanged(position)
        }
    }

    fun removeEntry(position: Int) {
        if (position >= 0 && position < resources.size) {
            resources.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}