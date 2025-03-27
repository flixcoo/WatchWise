package com.github.nullsafe.watchwise.profile.network

import com.github.nullsafe.watchwise.profile.data.remote.UserApi
import com.github.nullsafe.watchwise.profile.data.repository.UserRepositoryImpl
import com.github.nullsafe.watchwise.profile.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi): UserRepository =
        UserRepositoryImpl(api)
}
