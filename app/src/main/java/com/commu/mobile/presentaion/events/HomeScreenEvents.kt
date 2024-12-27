package com.commu.mobile.presentaion.events

import androidx.lifecycle.Lifecycle

sealed interface HomeScreenEvents {
    data class SearchChats(val text:String): HomeScreenEvents
    data object OnSearchClick: HomeScreenEvents
    data object OnChatItemClick: HomeScreenEvents
    data object OnPlusButtonClick :HomeScreenEvents
    data class RequestDialog(val request: Boolean): HomeScreenEvents
    data class OnLifecycleChange(val event: Lifecycle.Event): HomeScreenEvents
}