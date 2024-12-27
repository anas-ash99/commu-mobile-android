package com.commu.mobile.data.network

import com.commu.mobile.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("/user")
    suspend fun getAllUsers(): List<User>

    @GET("/user/friend/{userId}")
    suspend fun getFriends(@Path("userId") userId: String): ArrayList<User>
}