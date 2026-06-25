package com.me.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.me.data.datasource.local.dao.MovieDao
import com.me.data.datasource.local.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    companion object {
        const val DATABASE_NAME = "movie_db"

        fun getInstance(app: Context): AppDatabase =
            Room.databaseBuilder(
                context = app,
                klass = AppDatabase::class.java,
                name = DATABASE_NAME
            ).build()

    }
}