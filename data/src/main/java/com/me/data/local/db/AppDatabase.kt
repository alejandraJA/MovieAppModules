package com.me.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.me.data.local.dao.MovieDao
import com.me.data.local.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}