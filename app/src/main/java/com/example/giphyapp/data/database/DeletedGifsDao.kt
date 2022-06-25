package com.example.giphyapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giphyapp.data.model.DownsampledImageURL
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedGifsDao {

    @Query("SELECT * FROM gifs")
    fun getAllGifs(): Flow<List<DownsampledImageURL>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGif(url: DownsampledImageURL)
}