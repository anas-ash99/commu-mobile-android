package com.commu.mobile.presentaion.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commu.mobile.data.reposotiry.UserRepository
import com.commu.mobile.data.db.Data.loggedInUser
import com.commu.mobile.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    val allUsers = userRepository.getAllUsers()

    var loggedUser: User? = userRepository.getLoggedInUser()


    init {
        loggedInUser = loggedUser
    }
    fun saveUser(user: User){
        userRepository.saveUser(user)
        loggedUser = user
        loggedInUser = user
    }


}