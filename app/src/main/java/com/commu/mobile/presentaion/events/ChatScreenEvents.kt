package com.commu.mobile.presentaion.events

import androidx.compose.ui.text.input.TextFieldValue
import com.commu.mobile.model.Message

sealed interface ChatScreenEvents {
    data class TypeMessage(val text:TextFieldValue): ChatScreenEvents
    data class OnMessageSeen(val message: Message): ChatScreenEvents
    data class OnScroll(val firstVisibleMessage: Int): ChatScreenEvents
    data object OnStopScrolling :ChatScreenEvents
    data object OnSendClick :ChatScreenEvents
    data object OnMicClick :ChatScreenEvents
    data object OnUserHeaderClick :ChatScreenEvents

}