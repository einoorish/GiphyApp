package com.example.giphyapp.data.api

import com.example.giphyapp.data.model.ApiResponse

class GiphyApiHelper(private val apiService: GiphyApiService) {

    suspend fun getGifs(searchPhrase: String, offset: Int): ApiResponse = apiService.getGifs(searchPhrase, offset)
}