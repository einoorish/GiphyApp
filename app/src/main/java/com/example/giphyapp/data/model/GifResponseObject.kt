package com.example.giphyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class GifObject(@SerializedName("images") val images: GifImageURL) {
    constructor() : this(GifImageURL(DownsampledImageURL(""),DownsampledImageURL("")))
}

data class GifImageURL(@SerializedName("fixed_width_downsampled") val downsampledImage: DownsampledImageURL,
                       @SerializedName("original") val originalImage: DownsampledImageURL)


@Entity(tableName = "gifs")
class DownsampledImageURL(@PrimaryKey @SerializedName("url") val url: String){
    override fun equals(other: Any?): Boolean {
        if(other is DownsampledImageURL)
            return url == (other as DownsampledImageURL).url
        return super.equals(other)
    }
}
