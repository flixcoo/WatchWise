package com.github.nullsafe.watchwise.core.data.database.dao

import androidx.room.*
import com.github.nullsafe.watchwise.core.data.database.entity.MovieDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailDao {

    @Query("SELECT * FROM MovieDetail WHERE id = :id AND username = :username")
    suspend fun getById(id: Int, username: String): MovieDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(movieDetail: MovieDetail)

    @Delete
    suspend fun delete(movieDetail: MovieDetail)

    @Transaction
    suspend fun toggleMovieLike(movie: MovieDetail) {
        val existing = getById(movie.id, movie.username)
        if (existing == null) {
            save(movie.copy(liked = true))
        } else {
            save(existing.copy(liked = !existing.liked))
        }
    }

    @Transaction
    suspend fun toggleMovieWatched(movie: MovieDetail) {
        val existing = getById(movie.id, movie.username)
        if (existing == null) {
            save(movie.copy(watched = true))
        } else {
            save(existing.copy(watched = !existing.watched, unwatched = false))
        }
    }

    @Transaction
    suspend fun toggleMovieUnwatched(movie: MovieDetail) {
        val existing = getById(movie.id, movie.username)
        if (existing == null) {
            save(movie.copy(unwatched = true))
        } else {
            save(existing.copy(unwatched = !existing.unwatched, watched = false))
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM MovieDetail WHERE id = :id AND username = :username AND liked = 1)")
    suspend fun isMovieSaved(id: Int, username: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM MovieDetail WHERE id = :id AND username = :username AND watched = 1)")
    suspend fun isMovieWatched(id: Int, username: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM MovieDetail WHERE id = :id AND username = :username AND unwatched = 1)")
    suspend fun isMovieUnwatched(id: Int, username: String): Boolean

    @Query("SELECT * FROM MovieDetail WHERE liked = 1 AND username = :username")
    fun getSavedMovies(username: String): Flow<List<MovieDetail>>

    @Query("SELECT * FROM MovieDetail WHERE unwatched = 1 AND username = :username")
    fun getMoviesUnwatch(username: String): Flow<List<MovieDetail>>

    @Query("SELECT * FROM MovieDetail WHERE watched = 1 AND username = :username")
    fun getMoviesWatched(username: String): Flow<List<MovieDetail>>

    @Query("SELECT * FROM MovieDetail WHERE liked = 1 AND username = :username")
    fun getMoviesFavourites(username: String): Flow<List<MovieDetail>>
}