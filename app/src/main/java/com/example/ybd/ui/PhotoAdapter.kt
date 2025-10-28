package com.example.ybd.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ybd.R
import com.example.ybd.data.PhotoItem

class PhotoAdapter(private var items: List<PhotoItem>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<PhotoItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val captionTextView: TextView = itemView.findViewById(R.id.captionTextView)

        fun bind(item: PhotoItem) {
            captionTextView.text = item.caption

            if (item.photoResId != null) {
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(item.photoResId)
            } else {
                imageView.visibility = View.GONE // Скрываем ImageView
            }
        }
    }
}