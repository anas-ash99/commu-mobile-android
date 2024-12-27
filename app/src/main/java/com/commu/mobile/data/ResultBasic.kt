package com.commu.mobile.data

sealed class ResultBasic<out R> {
    data class Success<out T>(val data:T): ResultBasic<T>()
    data class Error(val exception: Exception): ResultBasic<Nothing>()
}