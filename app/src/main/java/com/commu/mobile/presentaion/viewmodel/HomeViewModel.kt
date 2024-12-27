package com.commu.mobile.presentaion.viewmodel

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commu.mobile.data.db.Data.loggedInUser
import com.commu.mobile.data.network.WebSocketClient
import com.commu.mobile.data.reposotiry.ChatRepository
import com.commu.mobile.data.reposotiry.MessageRepository
import com.commu.mobile.data.reposotiry.UserRepository
import com.commu.mobile.model.Chat
import com.commu.mobile.model.Message
import com.commu.mobile.model.User
import com.commu.mobile.model.WsMessage
import com.commu.mobile.presentaion.events.HomeScreenEvents
import com.commu.mobile.presentaion.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val state = MutableStateFlow(HomeState())
    val friends: Flow<List<User>> = userRepository.friends
    val chats: Flow<List<Chat>> = chatRepository.getChats()
    val lastMessages: Flow<List<Message>> = messageRepository.getLastMessages()
    private val websocketBaseUrl = "ws://192.168.0.82:8080/ws"

    init {
        syncFriends()
        syncChats()
        syncMessages()
        handleWebsocket()
    }

    private fun syncFriends() {
        viewModelScope.launch {
            userRepository.syncFriends(loggedInUser!!.id)
        }
    }

    private fun syncMessages() {
        viewModelScope.launch {
            messageRepository.syncMessages(loggedInUser!!.id)
        }
    }

    private fun handleWebsocket() {
        viewModelScope.launch {
            try {
                val url = "$websocketBaseUrl?clientID=${loggedInUser!!.id}-android"
                webSocketClient.connect(url)
            } catch (e: Exception) {
                Log.e("Websocket error", "Cannot connect to websocket")
            }
        }
        viewModelScope.launch {
            webSocketClient.messages.collect {
                val message = Json.decodeFromString<WsMessage>(it)
                if (message.type == "update_user") {
                    val user = Json.decodeFromString<User>(message.data)
                    userRepository.updateFriendLocally(user)
                } else if (message.type == "chat_message") {
                    val chatMessage = Json.decodeFromString<Message>(message.data)
                    if (chats.first()
                            .indexOfFirst { chat -> chat.id == chatMessage.chatId } == -1
                    ) {
                        createNewChat(chatMessage)
                    }
                    messageRepository.saveMessage(chatMessage)
                }
            }
        }
    }

    private fun createNewChat(message: Message) {
        val chat = Chat(
            id = message.chatId,
            users = listOf(loggedInUser!!.id, message.authorUserId)
        )
        viewModelScope.launch {
            chatRepository.saveChatLocally(chat)
        }
    }

    fun onEvent(event: HomeScreenEvents) {
        when (event) {
            is HomeScreenEvents.SearchChats -> {

            }

            HomeScreenEvents.OnSearchClick -> {

            }

            is HomeScreenEvents.RequestDialog -> {
                state.update { body ->
                    body.copy(
                        isUserListDialogShown = event.request
                    )
                }
            }

            HomeScreenEvents.OnPlusButtonClick -> {
                state.update { body ->
                    body.copy(
                        isUserListDialogShown = true
                    )
                }
            }

            HomeScreenEvents.OnChatItemClick -> {

            }

            is HomeScreenEvents.OnLifecycleChange -> {
                handleLifecycleEventChange(event.event)
            }
        }

    }

    private fun handleLifecycleEventChange(event: Lifecycle.Event) {

        when (event) {
            Lifecycle.Event.ON_RESUME -> loggedInUser?.isOnline = true
            Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> loggedInUser?.isOnline = false
            else -> return
        }
        val wsMessage = WsMessage(type = "update_user")
        val jsonUser = Json.encodeToJsonElement(loggedInUser)
        wsMessage.data = jsonUser.toString()
        val wsMessageJson = Json.encodeToJsonElement(wsMessage).toString()

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    webSocketClient.sendMessage(wsMessageJson, websocketBaseUrl)
                }
            } catch (e: Exception) {
                println("Failed to send WebSocket message: ${e.message}")
            }
        }
    }

    private fun syncChats() {
        viewModelScope.launch {
            chatRepository.syncChats(loggedInUser!!.id)
        }
    }
}






























