package com.example.giphyapp.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gif_ids")
class GifObject(@Ignore @SerializedName("images") val images: GifImageURL, @PrimaryKey var id: String) {
    constructor() : this(GifImageURL(DownsampledImageURL(""), DownsampledImageURL("")), "")
}

data class GifImageURL(@SerializedName("fixed_width_downsampled") val downsampledImage: DownsampledImageURL,
                       @SerializedName("original") val originalImage: DownsampledImageURL)


data class DownsampledImageURL(@PrimaryKey @SerializedName("url") val url: String)
