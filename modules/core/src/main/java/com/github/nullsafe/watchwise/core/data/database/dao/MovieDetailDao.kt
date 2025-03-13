package com.github.nullsafe.watchwise.core.data.database.dao

import androidx.room.*
import com.github.nullsafe.watchwise.core.data.database.entity.MovieDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailDao {

    @Query("SELECT * FROM MovieDetail WHERE id = :id")
    suspend fun getById(id: Int): MovieDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(movieDetail: MovieDetail)

    @Delete
    suspend fun delete(movieDetail: MovieDetail)

    @Transaction
    suspend fun toggleMovieLike(movie: MovieDetail) {
        val existing = getById(movie.id)
        if (existing == null) {
            save(movie.copy(liked = true))
        } else {
            delete(movie)
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM MovieDetail WHERE id = :id)")
    suspend fun isMovieSaved(id: Int): Boolean

    @Query("SELECT * FROM MovieDetail WHERE liked = 1")
    fun getSavedMovies(): Flow<List<MovieDetail>>
}