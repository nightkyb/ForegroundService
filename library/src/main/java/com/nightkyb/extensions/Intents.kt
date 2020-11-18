@file:Suppress("unused")

package com.nightkyb.extensions

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Intents
 *
 * @author nightkyb created at 2019/5/16 16:08
 */

inline fun <reified T : Activity> Context.startActivity() =
    startActivity(Intent(this, T::class.java))

inline fun <reified T : Activity> Context.startActivity(extras: Bundle) =
    startActivity(Intent(this, T::class.java).apply { putExtras(extras) })

inline fun <reified T : Activity> Fragment.startActivity() =
    startActivity(Intent(requireActivity(), T::class.java))

inline fun <reified T : Activity> Fragment.startActivity(extras: Bundle) =
    startActivity(Intent(requireActivity(), T::class.java).apply { putExtras(extras) })

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int) =
    startActivityForResult(Intent(this, T::class.java), requestCode)

inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    extras: Bundle
) = startActivityForResult(Intent(this, T::class.java).apply { putExtras(extras) }, requestCode)

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int) =
    startActivityForResult(Intent(requireActivity(), T::class.java), requestCode)

inline fun <reified T : Activity> Fragment.startActivityForResult(
    requestCode: Int,
    extras: Bundle
) = startActivityForResult(
    Intent(requireActivity(), T::class.java).apply { putExtras(extras) },
    requestCode
)

inline fun <reified T : Activity> Context.startActivityWithAnimation(
    enterResId: Int = 0,
    exitResId: Int = 0
) {
    val intent = Intent(this, T::class.java)
    val bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterResId, exitResId).toBundle()
    ContextCompat.startActivity(this, intent, bundle)
}

inline fun <reified T : Service> Context.startService(): ComponentName? =
    startService(Intent(this, T::class.java))

inline fun <reified T : Service> Context.startService(extras: Bundle): ComponentName? =
    startService(Intent(this, T::class.java).apply { putExtras(extras) })

inline fun <reified T : Service> Fragment.startService(): ComponentName? =
    requireActivity().startService(Intent(requireActivity(), T::class.java))

inline fun <reified T : Service> Fragment.startService(extras: Bundle): ComponentName? =
    requireActivity().startService(
        Intent(requireActivity(), T::class.java).apply { putExtras(extras) }
    )

inline fun <reified T : Service> Context.stopService(): Boolean =
    stopService(Intent(this, T::class.java))

inline fun <reified T : Service> Fragment.stopService(): Boolean =
    requireActivity().stopService(Intent(requireActivity(), T::class.java))

fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

fun Intent.newDocument(): Intent = apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
    } else {
        @Suppress("DEPRECATION")
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
    }
}

fun Intent.excludeFromRecents(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }

fun Fragment.browse(url: String, newTask: Boolean = false): Boolean =
    requireActivity().browse(url, newTask)

fun Context.browse(url: String, newTask: Boolean = false): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }
}

fun Fragment.share(text: String, subject: String = ""): Boolean =
    requireActivity().share(text, subject)

fun Context.share(text: String, subject: String = ""): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, null))
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }
}

fun Fragment.email(email: String, subject: String = "", text: String = ""): Boolean =
    requireActivity().email(email, subject, text)

fun Context.email(email: String, subject: String = "", text: String = ""): Boolean {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    if (subject.isNotEmpty()) {
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    if (text.isNotEmpty()) {
        intent.putExtra(Intent.EXTRA_TEXT, text)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}

// 需要申请CALL_PHONE权限
@RequiresPermission(Manifest.permission.CALL_PHONE)
fun Fragment.makeCall(number: String): Boolean = requireActivity().makeCall(number)

@RequiresPermission(Manifest.permission.CALL_PHONE)
fun Context.makeCall(number: String): Boolean {
    return try {
        // 需要申请CALL_PHONE权限
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Fragment.openDial(number: String): Boolean = requireActivity().openDial(number)

fun Context.openDial(number: String): Boolean {
    return try {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Fragment.sendSMS(number: String, text: String = ""): Boolean =
    requireActivity().sendSMS(number, text)

fun Context.sendSMS(number: String, text: String = ""): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
        intent.putExtra("sms_body", text)
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
