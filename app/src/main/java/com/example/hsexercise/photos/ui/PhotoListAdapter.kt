package com.example.hsexercise.photos.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.hsexercise.R
import com.example.hsexercise.photos.model.Photo
import kotlinx.android.synthetic.main.photo_item.view.*

/**
 * Adapter for recycler view
 * Showing author, dimensions and image
 * Show circular progress drawable while loading image
 * In case if image loading fails - display placeholder image
 */

class PhotoListAdapter internal constructor(
    private val context: Context
) : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var photos = emptyList<Photo>() // Cached copy of words

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorTextView = itemView.authorTextView
        val dimensionsTextView = itemView.dimensionsTextView
        val imageView = itemView.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView = inflater.inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val current = photos[position]
        holder.authorTextView.text = current.author
        holder.dimensionsTextView.text = String.format("%d x %d", current.width, current.height)

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context)
            .load(current.download_url)
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_photo_grey_48dp)
            .centerCrop()
            .into(holder.imageView);
    }

    internal fun setPhotos(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    override fun getItemCount() = photos.size
}