package com.github.nullsafe.watchwise.core.data.database.dao

import androidx.room.*
import com.github.nullsafe.watchwise.core.data.database.entity.TvShowDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDetailDao {

    @Query("SELECT * FROM TvShowDetail WHERE id = :id AND username = :username")
    suspend fun getById(id: Int, username: String): TvShowDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(tvShowDetail: TvShowDetail)

    @Delete
    suspend fun delete(tvShowDetail: TvShowDetail)

    @Transaction
    suspend fun toggleTvShowLike(tvShow: TvShowDetail) {
        val existing = getById(tvShow.id, tvShow.username)
        if (existing == null) {
            save(tvShow.copy(liked = true))
        } else {
            save(existing.copy(liked = !existing.liked))
        }
    }

    @Transaction
    suspend fun toggleTvShowWatched(tvShow: TvShowDetail) {
        val existing = getById(tvShow.id, tvShow.username)
        if (existing == null) {
            save(tvShow.copy(watched = true, unwatched = false))
        } else {
            save(existing.copy(watched = !existing.watched, unwatched = false))
        }
    }

    @Transaction
    suspend fun toggleTvShowUnwatched(tvShow: TvShowDetail) {
        val existing = getById(tvShow.id, tvShow.username)
        if (existing == null) {
            save(tvShow.copy(unwatched = true, watched = false))
        } else {
            save(existing.copy(unwatched = !existing.unwatched, watched = false))
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM TvShowDetail WHERE id = :id AND username = :username AND liked = 1)")
    suspend fun isTvShowSaved(id: Int, username: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM TvShowDetail WHERE id = :id AND username = :username AND watched = 1)")
    suspend fun isTvShowWatched(id: Int, username: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM TvShowDetail WHERE id = :id AND username = :username AND unwatched = 1)")
    suspend fun isTvShowUnwatched(id: Int, username: String): Boolean

    @Query("SELECT * FROM TvShowDetail WHERE liked = 1 AND username = :username")
    fun getSavedTvShows(username: String): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE unwatched = 1 AND username = :username")
    fun getTvShowsUnwatched(username: String): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE watched = 1 AND username = :username")
    fun getTvShowsWatched(username: String): Flow<List<TvShowDetail>>

    @Query("SELECT * FROM TvShowDetail WHERE liked = 1 AND username = :username")
    fun getTvShowsFavourites(username: String): Flow<List<TvShowDetail>>
}
