package com.me.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.me.data.datasource.local.entities.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setMovie(movieEntityKts: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setMovies(list: List<MovieEntity>)

    @Query("SELECT * FROM movie")
    fun getMovies(): List<MovieEntity>

    @Query("DELETE FROM movie")
    fun deleteMovies()

    @Query("UPDATE movie SET `like` = :check WHERE id == :id")
    fun updateMovie(id: Int, check: Boolean)

    @Query("SELECT COUNT(*) FROM movie")
    fun countMovies(): Int
}