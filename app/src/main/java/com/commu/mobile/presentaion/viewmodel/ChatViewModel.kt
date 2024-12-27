package com.commu.mobile.presentaion.viewmodel

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.commu.mobile.R
import com.commu.mobile.data.db.Data.loggedInUser
import com.commu.mobile.data.network.WebSocketClient
import com.commu.mobile.data.reposotiry.ChatRepository
import com.commu.mobile.data.reposotiry.MessageRepository
import com.commu.mobile.data.reposotiry.UserRepository
import com.commu.mobile.model.Chat
import com.commu.mobile.model.Message
import com.commu.mobile.model.User
import com.commu.mobile.model.WsMessage
import com.commu.mobile.presentaion.events.ChatScreenEvents
import com.commu.mobile.presentaion.state.ChatScreenState
import com.commu.mobile.utils.TimeUtils
import com.commu.mobile.utils.getDateForChat
import com.commu.mobile.utils.getTimeForChatItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val chatRepository: ChatRepository,
    userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state = MutableStateFlow(ChatScreenState())

    private val _chatId = MutableStateFlow(savedStateHandle["chatId"] ?: "")
    private val userId = savedStateHandle["otherUserId"] ?: ""
    val userChattingWith = userRepository.getUser(userId)
    private var _userChattingWith = User()
    val chatId: StateFlow<String> = _chatId.asStateFlow()
    private var _messages: List<Message> = arrayListOf()
    private var job: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages = _chatId.flatMapLatest { id ->
        if (id.isNotBlank()) {
            messageRepository.getMessageForChat(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        userChattingWith.onEach {
            _userChattingWith = it ?: User()
        }.launchIn(viewModelScope)
        getChat(_chatId.value)
        messages.onEach {
            _messages = it
        }.launchIn(viewModelScope)

    }

    private fun getChat(id: String) {
        viewModelScope.launch {
            if (_chatId.value.isBlank()) {
               chatRepository.getChatByUserId(userId)
            }else{
                chatRepository.getChat(id).collect { foundChat ->
                    setChat(foundChat)
                }
            }
        }

    }

    private fun setChat(chat: Chat) {
        state.update { body ->
            body.copy(
                chat = chat
            )
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {

            if (state.value.messageContent.text.trim().isNotBlank()) {
                // if first message, create new chat
                if (state.value.chat.id.isBlank()) {
                    createNewChat()
                }
                val msg = Message(
                    id = UUID.randomUUID().toString(),
                    authorUserId = loggedInUser!!.id,
                    otherUserId = _userChattingWith.id,
                    chatId = state.value.chat.id,
                    content = state.value.messageContent.text.trim(),
                    createdAt = TimeUtils.formatCurrentDateTime()
                )
                messageRepository.sendMessage(msg)
                state.update { body ->
                    body.copy(
                        messageContent = TextFieldValue("")
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: ChatScreenEvents) {
        when (event) {
            is ChatScreenEvents.TypeMessage -> {
                state.update { body ->
                    body.copy(
                        messageContent = event.text,
                        trailingIcon = if (event.text.text.trim()
                                .isBlank()
                        ) R.drawable.mic2 else R.drawable.send3
                    )
                }
            }

            ChatScreenEvents.OnMicClick -> {}
            is ChatScreenEvents.OnSendClick -> {
                sendMessage()
            }

            is ChatScreenEvents.OnMessageSeen -> {
                markMessagesAsRead(event.message)
            }

            ChatScreenEvents.OnUserHeaderClick -> {

            }

            is ChatScreenEvents.OnScroll -> {
                if (_messages.size > 1) {
                    val date = _messages[event.firstVisibleMessage].createdAt.getDateForChat()
                    state.update { body ->
                        body.copy(
                            isDateLabelVisible = true,
                            dateLabelText = date
                        )
                    }
                    cancelJob()
                }

            }

            ChatScreenEvents.OnStopScrolling -> {
                if (_messages.size > 1) {
                    startJob()
                }
            }
        }
    }

    private fun startJob() {
        if (job?.isActive == true) {
            println("Job is already running.")
            return
        }
        job = viewModelScope.launch {
            if (isActive) { // Ensure the coroutine wasn't canceled
                println("Waiting for 3 seconds...")
                delay(3000)
                state.update { body ->
                    body.copy(
                        isDateLabelVisible = false
                    )
                }

            }
        }
    }

    // Function to cancel the job
    private fun cancelJob() {
        job?.cancel()
        println("Job cancelled.")
    }

    private suspend fun createNewChat() {
        val chat = Chat(
            id = UUID.randomUUID().toString(),
            users = listOf(loggedInUser!!.id, _userChattingWith.id),
            createdAt = TimeUtils.formatCurrentDateTime()
        )
        chatRepository.saveChat(chat)
        _chatId.value = chat.id
        state.value = state.value.copy(
            chat = chat
        )
    }

    private fun markMessagesAsRead(message: Message) {
        if (message.seenAt == null && message.authorUserId != loggedInUser!!.id) {
            message.seenAt = TimeUtils.formatCurrentDateTime()
            viewModelScope.launch {
                messageRepository.sendMessage(message)
            }

        }
    }

}