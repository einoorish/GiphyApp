package com.example.giphyapp.data.repository

import com.example.giphyapp.data.api.GiphyApiHelper
import com.example.giphyapp.data.database.GifsDatabase
import com.example.giphyapp.data.model.ApiResponse
import com.example.giphyapp.data.model.DownsampledImageURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MainRepository(private val apiHelper: GiphyApiHelper, private val db: GifsDatabase) {

    companion object {
        const val DEFAULT_SEARCH_PHRASE = "hi"
    }

    private val dao = db.deletedGifsDao()

    suspend fun getGifs(searchPhrase: String, offset: Int): ApiResponse {
        return apiHelper.getGifs(searchPhrase, offset)
    }

    fun getDeletedGifs(): Flow<List<DownsampledImageURL>> {
        return dao.getAllGifs()
    }

    suspend fun addGif(url: String) {
        dao.insertGif(DownsampledImageURL(url))
    }
}