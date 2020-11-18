@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.nightkyb.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dates
 *
 * @author nightkyb created at 2019/6/10 11:36
 */

inline fun getNowDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String =
    getDateString(Date(), format)

inline fun getDateString(date: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String =
    getDateString(Date(date), format)

fun getDateString(date: Date, format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)
}

fun Long.toDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun Date.toDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}

fun Calendar.toDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this.time)
}

inline fun String.toDateString(
    fromFormat: String = "yyyy-MM-dd HH:mm:ss",
    toFormat: String = "yyyy-MM-dd HH:mm:ss"
): String = toDate(fromFormat)?.toDateString(toFormat) ?: this

fun String.toDate(format: String = "yyyy-MM-dd HH:mm:ss"): Date? {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    var parsedDate: Date? = null
    try {
        parsedDate = dateFormat.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return parsedDate
}
