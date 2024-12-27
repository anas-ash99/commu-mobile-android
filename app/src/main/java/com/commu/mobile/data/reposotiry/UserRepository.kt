package com.commu.mobile.data.reposotiry

import android.util.Log
import com.commu.mobile.data.db.UserDao
import com.commu.mobile.data.network.UserApi
import com.commu.mobile.model.User
import com.commu.mobile.utils.SharedPreferencesHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val userApi: UserApi,
    private val userDao: UserDao
) {
    val friends: Flow<List<User>> = userDao.getFriends()
    private val userKey = "user"
    fun getLoggedInUser(): User? {
        val userString = sharedPreferencesHelper.getString(userKey, "")
        if (userString.isBlank()) {
            return null
        }
        return Json.decodeFromString<User>(userString)
    }

    fun getUser(userId:String):Flow<User?>{
        return userDao.getUser(userId)
    }

    fun getAllUsers(): Flow<List<User>> = flow{
        try {
            emit(userApi.getAllUsers())
        }catch (e:Exception){
            Log.e("fetching all users", e.message, e)
            emit(arrayListOf())
        }
    }


    suspend fun updateFriendLocally(user: User){
        try {
            userDao.saveUser(user)
        }catch (e:Exception){
            Log.e("updating friend", e.message, e)
        }
    }
     suspend fun syncFriends(userId: String) {
        try {
            val remoteFriends = userApi.getFriends(userId)
            if(friends != remoteFriends){
                userDao.saveFriends(remoteFriends)
            }
        }catch (e:Exception){
            Log.e("getting friends", e.message, e)
        }
    }
    fun saveUser(user:User){
        val userJson = Json.encodeToString(user)
        sharedPreferencesHelper.saveString(userKey, userJson)
    }

    fun logout(){
        sharedPreferencesHelper.deleteString(userKey)
    }

}