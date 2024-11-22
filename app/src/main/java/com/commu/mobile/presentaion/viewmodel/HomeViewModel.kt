package com.commu.mobile.presentaion.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.commu.mobile.model.Chat
import com.commu.mobile.model.Message
import com.commu.mobile.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val messages = generateSampleMessages()
    private var chats = generateChats()
    val loggedInUser = "user1"
    val usersChattingWith = generateUsers()

    private fun generateUsers(): List<User> {
        return listOf(
            User(
                id = "user1",
                name = "Anas Ashraf"
            ),
            User(
                id = "user2",
                name = "John Man"
            ),
            User(
                id = "user3",
                name = "Lee Mou"
            ),
        )
    }
    private fun generateChats(): List<Chat> {
        return listOf(
            Chat(
                id = "chat1",
                users = listOf("user1", "user2")
            ),
            Chat(
                id = "chat2",
                users = listOf("user1", "user3")
            )
        )
    }

    private fun sortChatsByLastMessage() {
        // Create a map to store the latest message timestamp for each chat
        val latestMessageMap = messages.groupBy { it.chatId }
            .mapValues { entry -> entry.value.maxByOrNull { it.createdAt }?.createdAt ?: 0L }

        // Sort chats based on the latest message timestamp
        chats = chats.sortedWith(compareByDescending { chat -> latestMessageMap[chat.id] ?: 0L })

    }

    fun getChats():List<Chat>{
        sortChatsByLastMessage()
        return chats
    }

    private fun generateSampleMessages(): List<Message> {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val now = sdf.format(Date())

        return listOf(
            Message(
                id = UUID.randomUUID().toString(),
                userId = "user123",
                chatId = "chat1",
                content = "Hey, how's it going? This is a very long text that might wrap or get truncated",
                createdAt = "2024-11-22T01:30:12",
                deliveredAt = now,
                seenAt = now
            ),
            Message(
                id = UUID.randomUUID().toString(),
                userId = "user789",
                chatId = "chat1",
                content = "I'm doing well, thanks for asking!",
                createdAt = "2024-11-22T01:30:11",
                deliveredAt = now,
                seenAt = now
            ),
            Message(
                id = UUID.randomUUID().toString(),
                userId = "user123",
                chatId = "chat2",
                content = "Are we still meeting tomorrow?",
                createdAt = "2024-11-22T01:40:11",
                deliveredAt = null,
                seenAt = null
            )
        )
    }

    @SuppressLint("NewApi")
    fun getLastMessage(id: String): Message? {
        return messages.filter { it.chatId == id }.maxByOrNull { LocalDateTime.parse(it.createdAt) }
    }

    fun getUser(users: List<String>): User {
        val userId = if (users[0] == loggedInUser) users[1] else users[0]
        return this.usersChattingWith.first { it.id == userId }
    }

    fun getUnreadMessagesCount(id: String): Int {
        return messages
            .filter { it.chatId == id }.count { it.seenAt == null }
    }

}






























