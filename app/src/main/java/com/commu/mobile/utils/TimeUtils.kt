package com.commu.mobile.utils

import android.os.Build
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TimeUtils {
    fun formatCurrentDateTime(): String {
        val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime.now()
        } else {
            return ""
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z z")
        return currentDateTime.format(formatter)
    }
}
