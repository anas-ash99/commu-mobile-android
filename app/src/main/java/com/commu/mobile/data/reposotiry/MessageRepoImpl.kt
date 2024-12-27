package com.commu.mobile.data.reposotiry

import android.util.Log
import com.commu.mobile.data.db.MessageDao
import com.commu.mobile.data.network.MessageApi
import com.commu.mobile.model.Message
import com.commu.mobile.model.MessageBody
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.net.ConnectException
import javax.inject.Inject

class MessageRepoImpl @Inject constructor(
    private val messageApi: MessageApi,
    private val messageDao: MessageDao
) : MessageRepository {

    override fun getMessageForChat(chatId: String): Flow<List<Message>>  {
        return messageDao.findMessagesByChat(chatId)
    }

    override suspend fun syncMessages(user: String){
        try {
            val messages = messageApi.getMessagesForUser(user)
            messageDao.clearMessages()
            messageDao.saveMessages(messages)
        } catch (e: KotlinNullPointerException) {
            Log.e("fetching messages for user", e.message, e)
            messageDao.saveMessages(arrayListOf())
        } catch (e: Exception) {
            Log.e("fetching messages for user", e.message, e)
        }
    }

    override suspend fun sendMessage(message: Message) {
        try {
            val messageJson = Json.encodeToJsonElement(message)
            val messageBody = MessageBody(to_clients = listOf(message.authorUserId, message.otherUserId), data = messageJson.toString())
            messageApi.sendMessage(messageBody)
            messageDao.saveMessage(message)
        }catch (e: ConnectException){
            messageDao.saveMessage(message)
        }
        catch (e: Exception) {
            Log.e("sending message",e.message, e)
        }
    }

    override suspend fun saveMessage(message: Message) {
        messageDao.saveMessage(message)
    }

    override fun getLastMessages(): Flow<List<Message>> {
       return messageDao.getLastMessages()
    }

}