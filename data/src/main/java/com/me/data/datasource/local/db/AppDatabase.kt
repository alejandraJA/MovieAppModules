package com.me.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.me.data.datasource.local.dao.MovieDao
import com.me.data.datasource.local.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
