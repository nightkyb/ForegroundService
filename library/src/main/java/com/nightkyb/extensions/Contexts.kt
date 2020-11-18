@file:Suppress("unused")

package com.nightkyb.extensions

import android.view.View
import androidx.fragment.app.Fragment

/**
 * Contexts
 *
 * @author nightkyb created at 2019/5/14 10:26
 */

fun Fragment.finish() = requireActivity().finish()

fun View.OnClickListener.clickOn(vararg views: View) {
    views.forEach { view -> view.setOnClickListener(this) }
}
