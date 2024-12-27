package com.commu.mobile.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.commu.mobile.model.Chat
import com.commu.mobile.model.Message
import com.commu.mobile.model.User

@Database(
    entities = [User::class, Message::class, Chat::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
}