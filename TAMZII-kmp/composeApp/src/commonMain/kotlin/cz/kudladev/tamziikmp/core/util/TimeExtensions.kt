package cz.kudladev.tamziikmp.core.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

//fun formatUnixTime(unixTime: Long): String {
//    val instant = Instant.fromEpochSeconds(unixTime)
//    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
//    return dateTime.toString() // You can format this string as needed
//}
//
//fun String.getFullTime(): String {
//    fun formatUnixTime(unixTime: Long): String {
//        val instant = Instant.fromEpochSeconds(unixTime)
//        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//        return dateTime.toJavaLocalDateTime().format(formatter)
//    }
//    return formatUnixTime(this.toLong())
//}

