package com.example.giphyapp.data.api

import com.example.giphyapp.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {

    @GET(value = "gifs/search")
    suspend fun getGifs(
        @Query("q") searchPhrase: String,
        @Query("offset") offset: Int
    ): ApiResponse

}