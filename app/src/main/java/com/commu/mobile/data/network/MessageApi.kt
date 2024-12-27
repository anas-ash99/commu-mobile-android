package com.commu.mobile.data.network

import com.commu.mobile.model.Message
import com.commu.mobile.model.MessageBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApi {

    @GET("/message?key=userId")
    suspend fun getMessagesForUser(@Query("value") userId: String): ArrayList<Message>

    @GET("/message?key=chatId")
    suspend fun getMessagesForChat(@Query("value") chatId: String): ArrayList<Message>

    @POST("/message")
    suspend fun sendMessage(@Body message: MessageBody)
}