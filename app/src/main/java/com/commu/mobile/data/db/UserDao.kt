package com.commu.mobile.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.commu.mobile.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getFriends(): Flow<List<User>>

    @Upsert
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUser(userId: String): Flow<User?>

    @Upsert
    suspend fun saveFriends(users: List<User>)
}
