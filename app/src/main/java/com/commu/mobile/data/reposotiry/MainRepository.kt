package com.commu.mobile.data.reposotiry

import com.commu.mobile.model.Chat
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getChats(userId:String) :Flow<Result<Chat>>


}