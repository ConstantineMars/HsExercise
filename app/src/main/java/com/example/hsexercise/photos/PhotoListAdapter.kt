package com.example.hsexercise.photos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsexercise.R
import com.example.hsexercise.photos.model.Photo

class PhotoListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<PhotoListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var photos = emptyList<Photo>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = photos[position]
        holder.wordItemView.text = current.author
    }

    internal fun setPhotos(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    override fun getItemCount() = photos.size
}