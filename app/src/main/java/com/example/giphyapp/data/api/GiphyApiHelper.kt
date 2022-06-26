package com.example.giphyapp.data.api

import com.example.giphyapp.data.model.ApiResponse
import javax.inject.Inject

class GiphyApiHelper @Inject constructor(private val apiService: GiphyApiService) {
    suspend fun getGifs(searchPhrase: String, offset: Int): ApiResponse = apiService.getGifs(searchPhrase, offset)
}
