package com.me.data.di

import android.content.Context
import androidx.room.Room
import com.me.data.datasource.local.dao.MovieDao
import com.me.data.datasource.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import java.nio.charset.StandardCharsets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    const val DATABASE_NAME = "movie_db"
    private const val DATABASE_PASSPHRASE = "change-this-passphrase"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase  {
        System.loadLibrary("sqlcipher")

        val passphrase = DATABASE_PASSPHRASE.toByteArray(StandardCharsets.UTF_8)
        val factory = SupportOpenHelperFactory(passphrase)

        return Room.databaseBuilder(
                context = app,
                klass = AppDatabase::class.java,
                name = DATABASE_NAME
            )
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()
}
