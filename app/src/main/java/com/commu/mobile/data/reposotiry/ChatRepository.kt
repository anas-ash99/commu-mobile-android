package com.commu.mobile.data.reposotiry


import com.commu.mobile.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun syncChats(userId: String)
    fun getChats():Flow<List<Chat>>
    suspend fun getChat(id: String): Flow<Chat>
    suspend fun saveChat(chat: Chat)
    suspend fun saveChatLocally(chat: Chat)
    suspend fun getChatByUserId(userId: String):Chat?
}