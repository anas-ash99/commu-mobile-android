package com.commu.mobile.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale


@SuppressLint("NewApi")
fun String.toAgeInHours(): Long {
    // Parse the input date-time string with timezone
    val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val parsedDateTime = LocalDateTime.parse(this)

    // Get the current date-time with the same timezone
    val currentDateTime = LocalDateTime.now()

    // Calculate and return the difference in hours
    return ChronoUnit.HOURS.between(parsedDateTime, currentDateTime)
}

@SuppressLint("NewApi")
fun String.getTimeForChatItem(): String {
    try {
        val parsedDateTime = LocalDateTime.parse(this)

        val currentDateTime = LocalDateTime.now()
        println("day :${parsedDateTime.dayOfMonth}")
        println("current :${currentDateTime.dayOfMonth}")
        println("year :${parsedDateTime.year}")
        println("month :${parsedDateTime.month}")
        if (
            parsedDateTime.dayOfMonth == currentDateTime.dayOfMonth &&
            parsedDateTime.year == currentDateTime.year &&
            parsedDateTime.month == currentDateTime.month
            ){
            if (
                ChronoUnit.HOURS.between(parsedDateTime, currentDateTime).toInt() == 0
            ){
                return "${ChronoUnit.MINUTES.between(parsedDateTime, currentDateTime)}m"
            }
            return "${ChronoUnit.HOURS.between(parsedDateTime, currentDateTime)}h"
        }else if (
            parsedDateTime.year == currentDateTime.year &&
            parsedDateTime.month == currentDateTime.month &&
            currentDateTime.dayOfMonth - 1 == parsedDateTime.dayOfMonth
        ) {
            return "Yesterday"
        }else{

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
//            return "${parsedDateTime.monthValue + 1}/${parsedDateTime.dayOfMonth}/${parsedDateTime.year.toString().slice(2..parsedDateTime.year.toString().lastIndex)}"
            val date: Date = inputFormat.parse(this) ?: return ""
            return outputFormat.format(date)

        }
    }catch (e:Exception){
        Log.e("error parsing date", e.message, e)
        return ""
    }

}


