package com.commu.mobile.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.commu.mobile.data.db.Converters
import kotlinx.serialization.Serializable

@Entity(tableName = "chats")
@Serializable
data class Chat(
    @PrimaryKey
    var id:String = "",
    var users:List<String> = arrayListOf(),
    var createdAt:String = "",
    var isOnline: Boolean = false,
    var unseenMessagesCount:Int = 0
)
