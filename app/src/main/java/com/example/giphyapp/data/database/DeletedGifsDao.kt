package com.example.giphyapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giphyapp.data.model.GifObject
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedGifsDao {

    @Query("SELECT * FROM gif_ids")
    fun getAllGifs(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGif(id: GifObject)
}