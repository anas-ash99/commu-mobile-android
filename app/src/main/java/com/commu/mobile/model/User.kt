package com.commu.mobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "users")
@Serializable
data class User (
    @PrimaryKey
    var id:String = "",
    var name:String = "",
    var phoneNumber:String = "",
    var isOnline:Boolean = false,
    var friends:List<String> = arrayListOf(),
    var isTyping:Boolean = false,
    var createdAt:String = ""
)