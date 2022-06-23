package com.example.giphyapp.data.api

import com.example.giphyapp.data.response.ApiResponse

class GiphyApiHelper(private val apiService: GiphyApiService) {

    suspend fun getGifs(searchPhrase: String, offset: Int): ApiResponse = apiService.getGifs(searchPhrase, offset)
}