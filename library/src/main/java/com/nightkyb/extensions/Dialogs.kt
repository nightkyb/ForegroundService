@file:Suppress("unused")

package com.nightkyb.extensions

import android.app.Dialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * Dialogs
 *
 * @author nightkyb created on 2020/3/18.
 */

/**
 * 显示全屏沉浸式对话框，显示对话框时不会导致退出沉浸式。
 */
fun <T : AlertDialog.Builder> T.showImmersive(): AlertDialog {
    val dialog: AlertDialog = create()
    dialog.showImmersive()
    return dialog
}

/**
 * 显示全屏沉浸式对话框，显示对话框时不会导致退出沉浸式。
 */
fun <T : Dialog> T.showImmersive() {
    disabledFocusable(window)
    show()
    hideSystemUI(window)
    enabledFocusable(window)
}

/**
 * 显示全屏沉浸式对话框，显示对话框时不会导致退出沉浸式。
 */
/*fun <T : DialogFragment> T.showImmersive(manager: FragmentManager, tag: String?) {
    disabledFocusable(dialog?.window)
    show(manager, tag)
    hideSystemUI(dialog?.window)
    enabledFocusable(dialog?.window)
}*/

/**
 * dialog需要全屏的时候用，和enabledFocusable()成对出现。
 * 在show前调用disabledFocusable()，show后调用enabledFocusable()。
 */
private fun disabledFocusable(window: Window?) {
    window?.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    )
}

/**
 * dialog需要全屏的时候用，和disabledFocusable()成对出现。
 * 在show前调用disabledFocusable()，show后调用enabledFocusable()。
 */
private fun enabledFocusable(window: Window?) {
    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
}

/**
 * 进入全屏沉浸模式。
 */
fun hideSystemUI(window: Window?) {
    window?.decorView?.apply {
        systemUiVisibility = systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}

/**
 * 退出全屏沉浸模式。
 */
fun showSystemUI(window: Window?) {
    window?.decorView?.apply {
        // 先取非后再与，把对应位置的1置成0，原本为0的还是0
        systemUiVisibility = systemUiVisibility and
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE.inv() and
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION.inv() and
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv() and
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv() and
                View.SYSTEM_UI_FLAG_FULLSCREEN.inv() and
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
    }
}
