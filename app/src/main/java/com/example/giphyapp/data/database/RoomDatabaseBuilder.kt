package com.example.giphyapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object RoomDatabaseBuilder {

    fun buildDatabase(context: Context): GifsDatabase{
        return Room.databaseBuilder(context, GifsDatabase::class.java, "deleted_gifs_database").build()
    }
}