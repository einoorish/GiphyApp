package com.example.giphyapp.ui.viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.giphyapp.R
import com.example.giphyapp.databinding.ViewPagerBinding

class ViewPagerActivity: AppCompatActivity() {

    private lateinit var binding: ViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewPagerBinding.inflate(layoutInflater)
        setContentView(R.layout.view_pager)

        val position = intent.extras!!.getInt("id")
        val data = intent.getStringArrayListExtra("gif_urls")

        setupViewPager(position, data!!)
    }

    private fun setupViewPager(position: Int, data: List<String>) {
        val adapter = GifsPagerAdapter(data)
        val viewpager = findViewById<ViewPager2>(R.id.pager)

        viewpager.adapter = adapter
        viewpager.setCurrentItem(position, false)
    }

}