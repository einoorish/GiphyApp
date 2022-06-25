package com.example.giphyapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.giphyapp.data.model.DownsampledImageURL

@Database(entities = [DownsampledImageURL::class], version = 1,exportSchema = false)
abstract class GifsDatabase : RoomDatabase(){
    abstract fun deletedGifsDao(): DeletedGifsDao
}