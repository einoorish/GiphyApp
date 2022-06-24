package com.example.giphyapp.data.response

import com.google.gson.annotations.SerializedName

class GifObject(@SerializedName("images") val images: GifImageURL) {
    constructor() : this(GifImageURL(DownsampledImageURL(""),DownsampledImageURL("")))
}

data class GifImageURL(@SerializedName("fixed_width_downsampled") val downsampledImage: DownsampledImageURL,
                       @SerializedName("original") val originalImage: DownsampledImageURL)

data class DownsampledImageURL(@SerializedName("url") val url: String)
