package com.github.nullsafe.watchwise.profile.data.repository

import com.github.nullsafe.watchwise.profile.data.remote.UserApi
import com.github.nullsafe.watchwise.profile.data.remote.dto.UserRequest
import com.github.nullsafe.watchwise.profile.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    private val apiKey = "Gy7Jk0t7YqmGSz8IJQlXJ8EtM6dvmeC52o9jNuR2oK2BAzFuFa93iA=="

    override suspend fun register(username: String, password: String): Boolean {
        val response = api.registerUser(UserRequest(username, password), key = apiKey)
        return response.isSuccessful
    }

    override suspend fun login(username: String, password: String): Boolean {
        val response = api.loginUser(UserRequest(username, password), key = apiKey)
        return response.isSuccessful
    }

    override suspend fun update(username: String, newPassword: String): Boolean {
        val response = api.updateUser(UserRequest(username, newPassword), key = apiKey)
        return response.isSuccessful
    }

    override suspend fun delete(username: String): Boolean {
        val response = api.deleteUser(username, key = apiKey)
        return response.isSuccessful
    }
}

