package com.commu.mobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(tableName = "messages")
@Serializable
data class Message(
    @PrimaryKey
    var id: String = "",
    var authorUserId: String = "",
    var otherUserId: String = "",
    var chatId: String = "",
    var content: String = "",
    var createdAt: String = "",
    var deliveredAt: String? = null,
    var seenAt: String? = null
)
