package com.example.giphyapp.data.api

import com.example.giphyapp.data.response.ApiResponse
import com.example.giphyapp.data.response.GifObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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