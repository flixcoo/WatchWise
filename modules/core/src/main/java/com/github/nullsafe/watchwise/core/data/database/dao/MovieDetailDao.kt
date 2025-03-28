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
            save(existing.copy(liked = !existing.liked))
        }
    }

    @Transaction
    suspend fun toggleMovieWatched(movie: MovieDetail) {
        val existing = getById(movie.id)
        if (existing == null) {
            save(movie.copy(isWatched = true))
        } else {
            save(existing.copy(isWatched = !existing.isWatched, isUnwatched = false))
        }
    }

    @Transaction
    suspend fun toggleMovieUnwatched(movie: MovieDetail) {
        val existing = getById(movie.id)
        if (existing == null) {
            save(movie.copy(isUnwatched = true))
        } else {
            save(existing.copy(isUnwatched = !existing.isUnwatched, isWatched = false))
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM MovieDetail WHERE id = :id AND liked = 1)")
    suspend fun isMovieSaved(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM MovieDetail WHERE id = :id AND isWatched = 1)")
    suspend fun isMovieWatched(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM MovieDetail WHERE id = :id AND isUnwatched = 1)")
    suspend fun isMovieUnwatched(id: Int): Boolean

    @Query("SELECT * FROM MovieDetail WHERE liked = 1 AND username = :username")
    fun getSavedMovies(username: String): Flow<List<MovieDetail>>

    @Query("SELECT * FROM MovieDetail WHERE isUnwatched = 1 AND username = :username")
    fun getMoviesUnwatch(username: String): Flow<List<MovieDetail>>

    @Query("SELECT * FROM MovieDetail WHERE isWatched = 1 AND username = :username")
    fun getMoviesWatched(username: String): Flow<List<MovieDetail>>

    @Query("SELECT * FROM MovieDetail WHERE liked = 1 AND username = :username")
    fun getMoviesFavourites(username: String): Flow<List<MovieDetail>>
}