package com.commu.mobile.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.commu.mobile.model.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM chats")
    fun findAllChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun findChat(chatId: String): Chat?

    @Upsert
    suspend fun saveChat(chat: Chat)

    @Upsert
    suspend fun saveChats(chats: List<Chat>)

    @Query("DELETE FROM chats")
    suspend fun clearChats()

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteById(chatId: String)

    @Query("SELECT * FROM chats WHERE :userId IN (users) AND LENGTH(users) = 2")
    suspend fun getChatByUserId(userId: String):Chat?
}
