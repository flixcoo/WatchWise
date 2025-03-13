package com.github.nullsafe.watchwise.core.di.module

import com.github.nullsafe.watchwise.core.repository.DiscoverRepository
import com.github.nullsafe.watchwise.core.repository.GenresRepository
import com.github.nullsafe.watchwise.core.repository.MoviesRepository
import com.github.nullsafe.watchwise.core.repository.PeopleRepository
import com.github.nullsafe.watchwise.core.repository.SearchRepository
import com.github.nullsafe.watchwise.core.repository.TrendingRepository
import com.github.nullsafe.watchwise.core.repository.TvShowsRepository
import com.github.nullsafe.watchwise.core.repository.impl.DiscoverRepositoryImpl
import com.github.nullsafe.watchwise.core.repository.impl.GenresRepositoryImpl
import com.github.nullsafe.watchwise.core.repository.impl.MoviesRepositoryImpl
import com.github.nullsafe.watchwise.core.repository.impl.PeopleRepositoryImpl
import com.github.nullsafe.watchwise.core.repository.impl.SearchRepositoryImpl
import com.github.nullsafe.watchwise.core.repository.impl.TrendingRepositoryImpl
import com.github.nullsafe.watchwise.core.repository.impl.TvShowsRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindMoviesRepository(moviesRepository: MoviesRepositoryImpl): MoviesRepository

    @Binds
    fun bindSearchRepository(searchRepository: SearchRepositoryImpl): SearchRepository

    @Binds
    fun bindPeopleRepository(peopleRepository: PeopleRepositoryImpl): PeopleRepository

    @Binds
    fun provideTvShowsRepository(tvShowsRepository: TvShowsRepositoryImpl): TvShowsRepository

    @Binds
    fun bindTrendingRepository(trendingRepository: TrendingRepositoryImpl): TrendingRepository

    @Binds
    fun bindGenresRepository(genresRepository: GenresRepositoryImpl): GenresRepository

    @Binds
    fun bindDiscoverRepository(discoverRepository: DiscoverRepositoryImpl): DiscoverRepository
}