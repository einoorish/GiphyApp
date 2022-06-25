package com.example.giphyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.giphyapp.data.api.GiphyApiHelper
import com.example.giphyapp.data.database.GifsDatabase
import com.example.giphyapp.data.repository.MainRepository

class ViewModelFactory(private val apiHelper: GiphyApiHelper, private val db: GifsDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(MainRepository(apiHelper, db)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}