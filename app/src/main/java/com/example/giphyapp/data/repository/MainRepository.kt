package com.example.giphyapp.data.repository

import com.example.giphyapp.data.api.GiphyApiHelper
import com.example.giphyapp.data.database.GifsDatabase
import com.example.giphyapp.data.model.ApiResponse
import com.example.giphyapp.data.model.GifObject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: GiphyApiHelper, db: GifsDatabase) {

    private val dao = db.deletedGifsDao()

    suspend fun getGifs(searchPhrase: String, offset: Int): ApiResponse {
        return apiHelper.getGifs(searchPhrase, offset)
    }

    fun getDeletedGifIds(): Flow<List<String>> {
        return dao.getAllGifs()
    }

    suspend fun addGifId(gif: GifObject) {
        dao.insertGif(gif)
    }
}