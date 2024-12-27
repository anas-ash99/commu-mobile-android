package com.commu.mobile.data.reposotiry

import android.util.Log
import com.commu.mobile.data.db.ChatDao
import com.commu.mobile.data.db.MessageDao
import com.commu.mobile.data.db.UserDao
import com.commu.mobile.data.network.ChatApi
import com.commu.mobile.model.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val chatDao: ChatDao
) : ChatRepository {

    override fun getChats(): Flow<List<Chat>> {
        return chatDao.findAllChats()
    }

    override suspend fun syncChats(userId: String) {
        try {
            val remoteChats = chatApi.getChatsForUser(userId)
            chatDao.clearChats()
            chatDao.saveChats(remoteChats)
        } catch (e: Exception) {
            Log.e("syncing chats", e.message, e)
        }
    }

    override suspend fun getChat(id: String): Flow<Chat> = flow {
        try {
            emit(chatDao.findChat(id) ?: Chat())
        } catch (e: Exception) {
            Log.e("Fetching chat by id", e.message, e)
        }
    }

    override suspend fun saveChat(chat: Chat) {
        try {
            chatDao.saveChat(chat)
            chatApi.saveChat(chat)
        } catch (e: Exception) {
            Log.e("saving chat", e.message, e)
        }
    }

    override suspend fun saveChatLocally(chat: Chat) {
        try {
            chatDao.saveChat(chat)
        } catch (e: Exception) {
            Log.e("saving chat locally", e.message, e)
        }
    }

    override suspend fun getChatByUserId(userId: String): Chat? {
        return chatDao.getChatByUserId(userId)
    }

}