package com.commu.mobile.utils

import android.annotation.SuppressLint
import android.util.Log
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@SuppressLint("NewApi")
fun String.getTimeForChatItem(): String {
    try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z z")

        val parsedDateTime = ZonedDateTime.parse(this, formatter)
        val currentDateTime = ZonedDateTime.now()
        // when same day
        if (
            parsedDateTime.dayOfMonth == currentDateTime.dayOfMonth &&
            parsedDateTime.year == currentDateTime.year &&
            parsedDateTime.month == currentDateTime.month
        ) {
            if (ChronoUnit.HOURS.between(parsedDateTime, currentDateTime).toInt() == 0) {
                // handle minutes
                val min = ChronoUnit.MINUTES.between(parsedDateTime, currentDateTime).toInt()
                if (min == 0) {
                    return "now"
                }
                return "${min}m"
            }
            return "${ChronoUnit.HOURS.between(parsedDateTime, currentDateTime)}h"
        } else if (
            parsedDateTime.year == currentDateTime.year &&
            parsedDateTime.month == currentDateTime.month &&
            currentDateTime.dayOfMonth - 1 == parsedDateTime.dayOfMonth
        ) {
            return "Yesterday"
        } else {
            // Define the input date format
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z z")
            // Parse the input date string into ZonedDateTime
            val zonedDateTime = ZonedDateTime.parse(this, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
            val formattedDate = zonedDateTime.format(outputFormatter)
            return formattedDate
        }
    } catch (e: Exception) {
        Log.e("error parsing date", e.message, e)
        return ""
    }

}

@SuppressLint("NewApi")
fun String.getDateForChat():String{
    try {
        // Define the input date format
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z z")
        // Parse the input date string into ZonedDateTime
        val zonedDateTime = ZonedDateTime.parse(this, inputFormatter)
        val currentDateTime = ZonedDateTime.now()
        if (
            zonedDateTime.dayOfMonth == currentDateTime.dayOfMonth &&
            zonedDateTime.year == currentDateTime.year &&
            zonedDateTime.month == currentDateTime.month
        ) {
            return "Today"
        }
        val outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy") // Corrected to full month name
        val formattedDate = zonedDateTime.format(outputFormatter)
        return formattedDate
    }catch (e:Exception){
        Log.e("error parsing date", e.message, e)
        return ""
    }


}

@SuppressLint("NewApi")
fun String.getDayTime(): String {

    try {
        // Define the input date format
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z z")
        // Parse the input date string into ZonedDateTime
        val zonedDateTime = ZonedDateTime.parse(this, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedDate = zonedDateTime.format(outputFormatter)
        return formattedDate
    }catch (e:Exception){
        Log.e("error parsing date", e.message, e)
        return ""
    }
}


