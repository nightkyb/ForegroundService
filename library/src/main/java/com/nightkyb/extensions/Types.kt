@file:Suppress("unused")

package com.nightkyb.extensions

/**
 * Types
 *
 * @author nightkyb created at 2019/6/10 11:36
 */

inline fun <reified T> typeOf(): Class<T> {
    return T::class.java
}
