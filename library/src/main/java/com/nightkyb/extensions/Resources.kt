@file:Suppress("unused")

package com.nightkyb.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Resources
 *
 * @author nightkyb created at 2019/5/14 10:26
 */

fun Context.getColorCompat(@ColorRes colorId: Int): Int =
    ContextCompat.getColor(this, colorId)

fun Fragment.getColorCompat(@ColorRes colorId: Int): Int =
    ContextCompat.getColor(requireContext(), colorId)

fun Context.getColorStateListCompat(@ColorRes colorId: Int): ColorStateList =
    ContextCompat.getColorStateList(this, colorId)!!

fun Fragment.getColorStateListCompat(@ColorRes colorId: Int): ColorStateList =
    ContextCompat.getColorStateList(requireContext(), colorId)!!

fun Context.getDrawableCompat(@DrawableRes drawableId: Int): Drawable =
    ContextCompat.getDrawable(this, drawableId)!!

fun Fragment.getDrawableCompat(@DrawableRes drawableId: Int): Drawable =
    ContextCompat.getDrawable(requireContext(), drawableId)!!

fun Context.dp2px(dp: Float): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Fragment.dp2px(dp: Float): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun Fragment.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}
