@file:Suppress("unused")

package com.nightkyb.extensions

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * [Snackbar]的扩展类，提供[Snackbar]中没有的方法，可以用来改变背景颜色或者消息字体颜色。
 * 预设的提示样式：info、warn、alert、confirm。
 *
 * @author nightkyb created at 2019/4/19 17:55
 */

const val red = 0xFFF44336.toInt() // md_red_500
const val green = 0xFF4CAF50.toInt() // md_green_500
const val blue = 0xFF03A9F4.toInt() // md_light_blue_500
const val orange = 0xFFFF9800.toInt() // md_orange_500

fun Snackbar.setBackgroundColor(@ColorInt color: Int): Snackbar =
    apply { view.setBackgroundColor(color) }

fun Snackbar.info(): Snackbar = apply { view.setBackgroundColor(blue) }

fun Snackbar.warn(): Snackbar = apply { view.setBackgroundColor(orange) }

fun Snackbar.alert(): Snackbar = apply { view.setBackgroundColor(red) }

fun Snackbar.confirm(): Snackbar = apply { view.setBackgroundColor(green) }

fun View.snackbar(@StringRes message: Int): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

fun View.longSnackbar(@StringRes message: Int): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_LONG)
    .apply { show() }

fun View.indefiniteSnackbar(@StringRes message: Int): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .apply { show() }

fun View.snackbar(message: CharSequence): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

fun View.longSnackbar(message: CharSequence): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_LONG)
    .apply { show() }

fun View.indefiniteSnackbar(message: CharSequence): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .apply { show() }

fun View.snackbar(
    @StringRes message: Int,
    @StringRes actionText: Int,
    action: (View) -> Unit
): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .setAction(actionText, action)
    .apply { show() }

fun View.longSnackbar(
    @StringRes message: Int,
    @StringRes actionText: Int,
    action: (View) -> Unit
): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_LONG)
    .setAction(actionText, action)
    .apply { show() }

fun View.indefiniteSnackbar(
    @StringRes message: Int,
    @StringRes actionText: Int,
    action: (View) -> Unit
): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .setAction(actionText, action)
    .apply { show() }

fun View.snackbar(
    message: CharSequence,
    actionText: CharSequence,
    action: (View) -> Unit
): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .setAction(actionText, action)
    .apply { show() }

fun View.longSnackbar(
    message: CharSequence,
    actionText: CharSequence,
    action: (View) -> Unit
): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_LONG)
    .setAction(actionText, action)
    .apply { show() }

fun View.indefiniteSnackbar(
    message: CharSequence,
    actionText: CharSequence,
    action: (View) -> Unit
): Snackbar = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .setAction(actionText, action)
    .apply { show() }
