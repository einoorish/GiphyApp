package com.example.giphyapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.giphyapp.data.model.GifObject

@Database(entities = [GifObject::class], version = 2)
abstract class GifsDatabase : RoomDatabase(){
    abstract fun deletedGifsDao(): DeletedGifsDao
}