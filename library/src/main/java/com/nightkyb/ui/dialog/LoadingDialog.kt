package com.nightkyb.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nightkyb.databinding.DialogLoadingBinding
import com.nightkyb.extensions.dp2px

/**
 * 加载对话框。
 *
 * @author nightkyb created on 2020/6/2.
 */
class LoadingDialog : DialogFragment() {
    @StringRes
    private var messageId: Int? = null
    private var messageText: CharSequence? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogLoadingBinding.inflate(layoutInflater)
        val messageId = messageId
        val messageText = messageText

        when {
            messageId != null -> {
                binding.tvMessage.isVisible = true
                binding.tvMessage.setText(messageId)
            }
            messageText != null -> {
                binding.tvMessage.isVisible = true
                binding.tvMessage.text = messageText
            }
            else -> {
                binding.tvMessage.isVisible = false
            }
        }

        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        val screenWidth = resources.displayMetrics.widthPixels
        val inset = (screenWidth - binding.root.measuredWidth) / 2 - dp2px(10f)

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setBackgroundInsetStart(inset)
            .setBackgroundInsetEnd(inset)
            .create()
    }

    fun setMessage(@StringRes message: Int?) {
        messageId = message
        messageText = null
    }

    fun setMessage(message: CharSequence?) {
        messageText = message
        messageId = null
    }

    fun isShowing(): Boolean = dialog?.isShowing == true
}
