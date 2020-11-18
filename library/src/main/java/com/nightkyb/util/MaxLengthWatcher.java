package com.nightkyb.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 输入字符最大长度监听器。
 * <p>
 * EditText默认的maxLength属性无论英文还是中文都算一个字符，
 * 该Watcher的区别在于：1个英文算1个字符，1个中文算2个字符。
 *
 * @author nightkyb created at 2019/8/19 11:35
 */
public class MaxLengthWatcher implements TextWatcher {
    private EditText editText;
    private int maxLength;

    public MaxLengthWatcher(EditText editText, int maxLength) {
        this.editText = editText;
        this.maxLength = maxLength;
    }

    private int chineseLength(String str) {
        int valueLength = 0;
        if (!TextUtils.isEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if (isChinese(temp)) {
                    // 中文字符长度为2
                    valueLength += 2;
                } else {
                    // 其他字符长度为1
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    private boolean isChinese(String str) {
        boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if (!TextUtils.isEmpty(str)) {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (int i = 0; i < str.length(); i++) {
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if (!temp.matches(chinese)) {
                    isChinese = false;
                }
            }
        }
        return isChinese;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable editable) {
        int editStart = editText.getSelectionStart();
        int editEnd = editText.getSelectionEnd();
        // 先去掉监听器，否则会出现栈溢出
        editText.removeTextChangedListener(this);
        if (!TextUtils.isEmpty(editable)) {
            // 循环删除多出的字符
            while (chineseLength(editable.toString()) > maxLength) {
                editable.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
        }
        editText.setSelection(editStart);
        // 恢复监听器
        editText.addTextChangedListener(this);
    }
}
