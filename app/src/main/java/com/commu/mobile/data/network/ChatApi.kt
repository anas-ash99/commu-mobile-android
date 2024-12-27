package com.commu.mobile.data.network

import com.commu.mobile.model.Chat
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @GET("chat/{userId}")
    suspend fun getChatsForUser(@Path("userId") userId:String) :ArrayList<Chat>
    @POST("/chat")
    suspend fun saveChat(@Body chat: Chat)

}