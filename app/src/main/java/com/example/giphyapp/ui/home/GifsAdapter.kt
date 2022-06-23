package com.example.giphyapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.giphyapp.R
import com.example.giphyapp.data.response.GifObject
import com.example.giphyapp.data.response.DownsampledImageURL


class GifsAdapter(private val gifs: ArrayList<GifObject>) : RecyclerView.Adapter<ViewHolder>() {

    val FOOTER_VIEW = 1
    val DATA_VIEW = 0
    private var isLoadingAdded = false

    class FooterViewHolder(itemView: View) : ViewHolder(itemView)

    class DataViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind(gif: DownsampledImageURL) {
            itemView.apply {
                Glide.with(itemView.context)
                    .load(gif.url)
                    .into(itemView.findViewById(R.id.iv_gif))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == FOOTER_VIEW) {
            return FooterViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)))
        } else {
            return DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gif_item, parent, false))
        }
    }

    override fun getItemCount(): Int = gifs.size

    override fun getItemViewType(position: Int): Int {
        return if (isLoadingAdded && position == gifs.size-1) {
            FOOTER_VIEW
        } else DATA_VIEW
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is DataViewHolder) {
            val vh = holder as DataViewHolder
            vh.bind(gifs[position].images.downsampledImage)
        }
    }


    fun addLoadingFooter() {
        gifs.add(GifObject())
        isLoadingAdded = true
    }

    fun removeLoadingFooter() {
        val position = gifs.size-1
        if (isLoadingAdded) {
            gifs.removeAt(position)
            notifyItemRemoved(position)
        }
        isLoadingAdded = false
    }

    fun addNewGifs(gifs: List<GifObject>) {
        this.gifs.apply {
            clear()
            addAll(gifs)
        }
    }

    fun addNextGifs(gifs: List<GifObject>) {
        this.gifs.apply {
            addAll(gifs)
        }
    }

    fun getCurrentNumOfGifs():Int = gifs.size
}