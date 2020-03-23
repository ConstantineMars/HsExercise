package com.example.hsexercise.photos.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.hsexercise.R
import com.example.hsexercise.photos.model.Photo
import com.example.hsexercise.photos.state.StateData
import kotlinx.android.synthetic.main.footer_item.view.*
import kotlinx.android.synthetic.main.photo_item.view.*

/**
 * Adapter for recycler view
 * Showing author, dimensions and image
 * Show circular progress drawable while loading image
 * In case if image loading fails - display placeholder image
 */

class PhotoListAdapter internal constructor(
    private val context: Context,
    private val retry: () -> Unit
) : PagedListAdapter<Photo, RecyclerView.ViewHolder>(PhotoDiffCallback) {

    private var state = StateData.State.LOADING

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var photos = emptyList<Photo>() // Cached copy of words

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorTextView = itemView.authorTextView
        val dimensionsTextView = itemView.dimensionsTextView
        val imageView = itemView.imageView

        fun bind(current: Photo) {
            authorTextView.text = current.author
            dimensionsTextView.text = String.format("%d x %d", current.width, current.height)

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Glide.with(context)
                .load(current.download_url)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_photo_grey_48dp)
                .centerCrop()
                .into(imageView)
        }
    }

    inner class FooterViewHolder(retry: () -> Unit, view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.txt_error.setOnClickListener { retry() }
        }

        fun bind(status: StateData.State?) {
            itemView.progress_bar.visibility = if (status == StateData.State.LOADING) View.VISIBLE else View.INVISIBLE
            itemView.txt_error.visibility = if (status == StateData.State.ERROR) View.VISIBLE else View.INVISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            val itemView = inflater.inflate(R.layout.photo_item, parent, false)
            return PhotoViewHolder(itemView)
        } else {
            val itemView = inflater.inflate(R.layout.footer_item, parent, false)
            return FooterViewHolder(retry, itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            DATA_VIEW_TYPE -> getItem(position)?.let { (holder as PhotoViewHolder).bind(it) }
            else -> (holder as FooterViewHolder).bind(state)
        }
    }

    internal fun setPhotos(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int
    {
        val count = super.getItemCount() + if (hasFooter()) 1 else 0
        return count
    } // photos.size

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == StateData.State.LOADING || state == StateData.State.ERROR)
    }

    fun setState(state: StateData.State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        val PhotoDiffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.download_url == newItem.download_url
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }
}