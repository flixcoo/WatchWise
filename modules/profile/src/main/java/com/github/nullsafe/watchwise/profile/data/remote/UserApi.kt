package com.github.nullsafe.watchwise.profile.data.remote

import com.github.nullsafe.watchwise.profile.data.remote.dto.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @POST("user/register")
    suspend fun registerUser(
        @Body user: UserRequest,
        @Query("code") key: String
    ): Response<Unit>

    @POST("user/login")
    suspend fun loginUser(
        @Body user: UserRequest,
        @Query("code") key: String
    ): Response<Unit>

    @PUT("user/update")
    suspend fun updateUser(
        @Body user: UserRequest,
        @Query("code") key: String
    ): Response<Unit>

    @DELETE("user/delete/{username}")
    suspend fun deleteUser(
        @Path("username") username: String,
        @Query("code") key: String
    ): Response<Unit>
}
