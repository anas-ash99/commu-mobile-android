package com.commu.mobile.presentaion.state

import com.commu.mobile.model.Chat

data class HomeState(
    var chats:ArrayList<Chat> = arrayListOf(),
    val isChatsLoading :Boolean = false,
    val uiTrigger: Int = 0,
    val isUserListDialogShown:Boolean = false
)
