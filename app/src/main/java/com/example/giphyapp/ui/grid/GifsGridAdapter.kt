package com.example.giphyapp.ui.grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.giphyapp.data.model.GifObject
import com.example.giphyapp.databinding.GifItemBinding


class GifsGridAdapter(private val gifs: ArrayList<GifObject>, private val onLongClick: (url: String) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    val FOOTER_VIEW = 1
    val DATA_VIEW = 0
    private var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == FOOTER_VIEW) {
            return FooterViewHolder(GifItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return DataViewHolder(GifItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), ::getGifUrls)
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
            vh.bind(gifs[position].images.downsampledImage, position, onLongClick)
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

    fun getGifUrls(): ArrayList<String> {
        val gifUrls = ArrayList<String>()
        for(g in gifs){
            gifUrls.add(g.images.originalImage.url)
        }

        if(isLoadingAdded) gifUrls.removeAt(gifUrls.size - 1)

        return gifUrls
    }
}