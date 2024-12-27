package com.commu.mobile.model

data class MessageBody(
    var to_clients:List<String> = listOf(),
    var data:String = ""
)
