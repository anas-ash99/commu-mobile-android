package com.commu.mobile.model

import java.util.UUID

data class Message(
    var id: String = UUID.randomUUID().toString(),
    var userId: String = "",
    var chatId: String = "",
    var content: String = "",
    var createdAt: String = "",
    var deliveredAt: String? = null,
    var seenAt :String? = null
    )
