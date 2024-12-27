package com.commu.mobile.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.commu.mobile.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    fun findMessagesByChat(chatId: String): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE authorUserId = :userId")
    suspend fun findMessagesByUser(userId: String): List<Message>



    @Upsert
    suspend fun saveMessage(message: Message)

    @Upsert
    suspend fun saveMessages(messages: List<Message>)
    @Query("DELETE FROM messages")
    suspend fun clearMessages()

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChat(chatId: String)

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY createdAt DESC LIMIT 1")
    suspend fun findLastMessageForChat(chatId: String): Message?

    @Query("SELECT COUNT(*) FROM messages WHERE chatId = :chatId AND seenAt IS NULL")
    suspend fun getUnreadMessagesCount(chatId: String): Int

    @Query("""
        SELECT m1.*
        FROM messages m1
        JOIN (
            SELECT chatId, MAX(createdAt) AS latestTime
            FROM messages
            GROUP BY chatId
        ) m2 ON m1.chatId = m2.chatId AND m1.createdAt = m2.latestTime
        ORDER BY m2.latestTime DESC
    """)
    fun getLastMessages(): Flow<List<Message>>

}