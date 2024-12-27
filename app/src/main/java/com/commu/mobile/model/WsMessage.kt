package com.commu.mobile.model

import kotlinx.serialization.Serializable

@Serializable
data class WsMessage(
    var type:String = "",
    var data:String = ""
)
