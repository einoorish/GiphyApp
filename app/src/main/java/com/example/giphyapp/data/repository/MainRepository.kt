package com.example.giphyapp.data.repository

import com.example.giphyapp.data.api.GiphyApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class MainRepository(private val apiHelper: GiphyApiHelper) {
    suspend fun getGifs(searchPhrase: String, offset: Int) = withContext(Dispatchers.IO){
        sleep(1000) // TODO: remove later, this is just for testing whether loading_item works properly
        apiHelper.getGifs(searchPhrase, offset)
    }
}