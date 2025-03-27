package com.github.nullsafe.watchwise.profile.domain.repository

interface UserRepository {
    suspend fun register(username: String, password: String): Boolean
    suspend fun login(username: String, password: String): Boolean
    suspend fun update(username: String, newPassword: String): Boolean
    suspend fun delete(username: String): Boolean
}