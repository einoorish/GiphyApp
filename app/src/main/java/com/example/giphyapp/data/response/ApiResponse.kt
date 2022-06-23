package com.example.giphyapp.data.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("data") val gifs: List<GifObject>,
    @SerializedName("pagination") val pagination: PaginationResponseObject
)
