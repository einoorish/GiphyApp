package com.example.giphyapp.ui.grid

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.ContextThemeWrapper
import android.view.View
import android.view.View.GONE
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.example.giphyapp.R
import com.example.giphyapp.data.model.DownsampledImageURL
import com.example.giphyapp.ui.viewpager.ViewPagerActivity
import com.bumptech.glide.request.target.Target
import com.example.giphyapp.databinding.GifItemBinding

class DataViewHolder(private val binding: GifItemBinding, private val getGifUrls: () -> ArrayList<String>): RecyclerView.ViewHolder(binding.root){

    fun bind(gif: DownsampledImageURL, position: Int, onLongClick: (url: String) -> Unit) {
        val itemView = binding.root
        Glide.with(itemView.context)
            .load(gif.url)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.gifProgress.visibility = GONE
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    binding.gifProgress.visibility = GONE
                    return false
                }
            })
            .transition(withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.ivGif)

        itemView.setOnClickListener{openInViewPagerActivity(position)}
        itemView.setOnLongClickListener{showPopup(itemView, gif, onLongClick)}
    }

    private fun showPopup(itemView: View, gif: DownsampledImageURL, onLongClick: (url: String) -> Unit): Boolean {
        val popupMenu = PopupMenu(ContextThemeWrapper(itemView.context, R.style.PopupMenu), itemView)
        popupMenu.inflate(R.menu.delete)
        popupMenu.setOnMenuItemClickListener {
            if(it.itemId == R.id.action_delete) 
                onLongClick(gif.url)
            true
        }
        popupMenu.show()
        return true
    }

    private fun openInViewPagerActivity(position: Int) {
        val i = Intent(itemView.context, ViewPagerActivity::class.java).apply {
            putExtra("id", position)
            putStringArrayListExtra("gif_urls", getGifUrls())
        }
        itemView.context.startActivity(i)
    }
}