package com.commu.mobile.data.reposotiry

import com.commu.mobile.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessageForChat(chatId:String):Flow<List<Message>>
    suspend fun syncMessages(user:String)
    suspend fun sendMessage(message: Message)
    suspend fun saveMessage(message: Message)
    fun getLastMessages(): Flow<List<Message>>
}