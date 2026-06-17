package com.me.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.me.data.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setMovie(movieEntityKts: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setMovies(list: List<MovieEntity>)

    @Query("SELECT * FROM movie")
    fun getMovies(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movie")
    fun deleteMovies()

    @Query("UPDATE movie SET `like` = :check WHERE id == :id")
    fun updateMovie(id: Int, check: Boolean)

    @Query("SELECT COUNT(*) FROM movie")
    fun countMovies(): Int
}