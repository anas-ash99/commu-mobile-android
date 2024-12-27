package com.commu.mobile.presentaion.state

import androidx.compose.ui.text.input.TextFieldValue
import com.commu.mobile.R
import com.commu.mobile.model.Chat
import com.commu.mobile.model.Message

data class ChatScreenState(
    val messages:ArrayList<Message> = arrayListOf(),
    val chat: Chat = Chat(),
    val scrollPosition:Int = 0,
    val messageContent:TextFieldValue = TextFieldValue(""),
    val trailingIcon:Int = R.drawable.mic2,
    val uiTrigger:Int = 0,
    val isDateLabelVisible:Boolean = false,
    val dateLabelText:String = ""
)
