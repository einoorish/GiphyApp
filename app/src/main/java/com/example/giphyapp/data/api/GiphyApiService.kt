package com.example.giphyapp.data.api

import com.example.giphyapp.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "r6F16GBdEMkeVtLp6472NDskhIpRdVL9"
const val ITEMS_PER_PAGE_LIMIT = 12 // should be phone-size dependent?

interface GiphyApiService {

    //TODO: add getPopularGIFs, so the first page is never empty?

    @GET(value = "gifs/search")
    suspend fun getGifs(
        @Query("q") searchPhrase: String,
        @Query("offset") offset: Int
    ): ApiResponse

}