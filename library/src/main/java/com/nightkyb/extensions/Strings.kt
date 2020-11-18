@file:Suppress("unused")

package com.nightkyb.extensions

import android.util.Base64
import java.security.MessageDigest
import java.util.*

/**
 * Strings
 *
 * @author nightkyb created at 2019/5/17 10:32
 */

fun String.fromBase64(): String = String(Base64.decode(this, Base64.DEFAULT))

fun String.toBase64(): String = Base64.encodeToString(toByteArray(), Base64.DEFAULT)

fun String.md5(): String = encrypt(this, "MD5")

fun String.sha1(): String = encrypt(this, "SHA-1")

fun String.isIdCard(): Boolean {
    val p18 =
        "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]\$".toRegex()
    val p15 =
        "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]\$".toRegex()
    return matches(p18) || matches(p15)
}

fun String.isPhone(): Boolean {
    val p = "^1[3-9]\\d{9}\$".toRegex()
    return matches(p)
}

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

/**
 * 只允许字母 + 数字的组合，不能纯字母或者纯数字，长度8-16位。
 */
fun String.isPassword(): Boolean {
    val p = "^(?![a-zA-Z]+\$)(?![0-9]+\$)[a-zA-Z0-9]{8,16}\$".toRegex()
    return matches(p)
}

fun String?.equalsIgnoreCase(other: String?): Boolean = equals(other, ignoreCase = true)

private fun encrypt(text: String, type: String): String {
    val bytes = MessageDigest.getInstance(type).digest(text.toByteArray())
    return bytes.toHexString()
}

fun ByteArray.toHexString(): String {
    val sb = StringBuilder()

    for (byte in this) {
        val hex = (byte.toInt() and 0xFF).toString(16)
        if (hex.length == 1) {
            sb.append("0")
        }
        sb.append(hex)
    }

    return sb.toString().toUpperCase(Locale.getDefault())
}

fun String.hexToBytes(): ByteArray {
    val length = length / 2
    val des = ByteArray(length)

    for (i in 0 until length) {
        des[i] = (this[i * 2].toString().toInt(16) shl 4 or this[i * 2 + 1].toString()
            .toInt(16)).toByte()
    }

    return des
}
