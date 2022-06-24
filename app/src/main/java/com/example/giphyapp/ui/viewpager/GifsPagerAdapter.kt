package com.example.giphyapp.ui.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giphyapp.databinding.FullGifItemBinding

class GifsPagerAdapter(private val urls: List<String>) : RecyclerView.Adapter<GifsPagerAdapter.DataViewHolder>() {

    class DataViewHolder(val binding: FullGifItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(gifUrl: String) {
            itemView.apply {
                Glide.with(binding.root.context)
                    .load(gifUrl)
                    .into(binding.ivGif)
            }
        }
    }

    override fun getItemCount(): Int = urls.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(FullGifItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int){
        holder.bind(urls[position])
    }

}