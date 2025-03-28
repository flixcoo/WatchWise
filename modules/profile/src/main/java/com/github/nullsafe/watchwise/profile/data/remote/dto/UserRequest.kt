package com.github.nullsafe.watchwise.profile.data.remote.dto

data class UserRequest(
    val username: String,
    val passwordHash: String
)
