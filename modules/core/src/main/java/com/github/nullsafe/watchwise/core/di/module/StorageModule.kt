package com.github.nullsafe.watchwise.core.di.module

import android.content.Context
import androidx.room.Room
import com.github.nullsafe.watchwise.core.data.database.AppDatabase
import com.github.nullsafe.watchwise.core.data.database.converter.*
import com.github.nullsafe.watchwise.core.data.database.dao.MovieDao
import com.github.nullsafe.watchwise.core.data.database.dao.TvShowDao
import com.github.nullsafe.watchwise.core.data.database.dao.TvShowDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    companion object {
        private const val DATABASE_NAME = "database"

        @Singleton
        @Provides
        fun providePersonDao(database: AppDatabase) = database.personDao()

        @Singleton
        @Provides
        fun providePersonDetailDao(database: AppDatabase) = database.personDetailDao()

        @Singleton
        @Provides
        fun provideMovieDetailDao(database: AppDatabase) = database.movieDetailDao()

        @Singleton
        @Provides
        fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()

        @Singleton
        @Provides
        fun provideTvShowDetailDao(database: AppDatabase): TvShowDetailDao =
            database.tvShowDetailDao()

        @Singleton
        @Provides
        fun provideTvShowDao(database: AppDatabase): TvShowDao = database.tvShowDao()

        @Singleton
        @Provides
        fun provideGenresDao(database: AppDatabase) = database.genresDao()

        @Singleton
        @Provides
        fun provideRoomDatabase(
            @ApplicationContext context: Context,
            listIntConverter: ListIntConverter,
            listStringConverter: ListStringConverter,
            genreConverter: GenreConverter,
            productionCompanyConverter: ProductionCompanyConverter,
            productionCountryConverter: ProductionCountryConverter,
            movieCreditsCastConverter: MovieCreditsCastConverter,
            knownForConverter: KnownForConverter,
            movieConverter: MovieConverter,
            tvShowConverter: TvShowConverter,
            createdByConverter: CreatedByConverter,
            networkConverter: NetworkConverter,
            seasonConverter: SeasonConverter,
            personCreditsCastConverter: PersonCreditsCastConverter
        ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addTypeConverter(listIntConverter)
            .addTypeConverter(listStringConverter)
            .addTypeConverter(genreConverter)
            .addTypeConverter(productionCompanyConverter)
            .addTypeConverter(productionCountryConverter)
            .addTypeConverter(personCreditsCastConverter)
            .addTypeConverter(movieCreditsCastConverter)
            .addTypeConverter(knownForConverter)
            .addTypeConverter(tvShowConverter)
            .addTypeConverter(createdByConverter)
            .addTypeConverter(networkConverter)
            .addTypeConverter(seasonConverter)
            .addTypeConverter(movieConverter)
            .fallbackToDestructiveMigration()
            .addMigrations(AppDatabase.MIGRATION_2_3)
            .addMigrations(AppDatabase.MIGRATION_3_4)
            .build()
    }
}