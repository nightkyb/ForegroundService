@file:Suppress("unused")

package com.nightkyb.extensions

import android.widget.EditText
import com.nightkyb.util.MaxLengthWatcher

/**
 * EditTexts
 *
 * @author nightkyb created at 2019/8/19 11:35
 */

/**
 * 设置输入字符最大长度。
 *
 * EditText默认的maxLength属性无论英文还是中文都算一个字符，
 * 该方法的区别在于：1个英文算1个字符，1个中文算2个字符。
 *
 * @see MaxLengthWatcher
 */
fun EditText.setMaxLength(maxLength: Int): EditText = apply {
    addTextChangedListener(MaxLengthWatcher(this, maxLength))
}
