@file:Suppress("unused")

package com.nightkyb.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * SharedPreferences工具类，使用了双重校验锁式(Double Check Lock)的单例模式。支持多个sp实例。
 *
 * @author nightkyb created at 2019/8/26 10:27
 */
class SpUtils private constructor(spName: String, mode: Int) {
    private val sp: SharedPreferences

    init {
        sp = context.getSharedPreferences(spName, mode)
    }

    companion object {
        private val SP_UTILS_MAP = hashMapOf<String, SpUtils>()
        private lateinit var context: Context
        private lateinit var SP_DEFAULT_NAME: String

        /**
         * 应用初始化时调用。
         */
        @JvmStatic
        fun init(context: Context) {
            this.context = context
            // 如果packageName = com.xxx.App，则sp默认名称为App
            SP_DEFAULT_NAME = context.packageName.substringAfterLast('.')
        }

        /**
         * 获取sp实例，单例模式。
         */
        @JvmStatic
        @JvmOverloads
        fun getInstance(
            spName: String = SP_DEFAULT_NAME,
            mode: Int = Context.MODE_PRIVATE
        ): SpUtils {
            val spUtils = SP_UTILS_MAP[spName]

            return spUtils ?: synchronized(this) {
                spUtils ?: SpUtils(spName, mode).also { SP_UTILS_MAP[spName] = it }
            }
        }
    }

    fun contains(key: String): Boolean = sp.contains(key)

    fun getString(key: String, defaultValue: String = ""): String =
        sp.getString(key, defaultValue) ?: defaultValue

    fun getStringSet(key: String, defaultValue: Set<String> = setOf()): Set<String> =
        sp.getStringSet(key, defaultValue) ?: defaultValue

    fun getInt(key: String, defaultValue: Int = -1): Int = sp.getInt(key, defaultValue)

    fun getLong(key: String, defaultValue: Long = -1L): Long = sp.getLong(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float = -1f): Float = sp.getFloat(key, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        sp.getBoolean(key, defaultValue)

    fun put(key: String, value: String, commit: Boolean = false) = sp.edit(commit = commit) {
        putString(key, value)
    }

    fun put(key: String, value: Set<String>, commit: Boolean = false) = sp.edit(commit = commit) {
        putStringSet(key, value)
    }

    fun put(key: String, value: Int, commit: Boolean = false) = sp.edit(commit = commit) {
        putInt(key, value)
    }

    fun put(key: String, value: Long, commit: Boolean = false) = sp.edit(commit = commit) {
        putLong(key, value)
    }

    fun put(key: String, value: Float, commit: Boolean = false) = sp.edit(commit = commit) {
        putFloat(key, value)
    }

    fun put(key: String, value: Boolean, commit: Boolean = false) = sp.edit(commit = commit) {
        putBoolean(key, value)
    }

    fun remove(key: String, commit: Boolean = false) = sp.edit(commit = commit) {
        remove(key)
    }

    fun clear(commit: Boolean = false) = sp.edit(commit = commit) {
        clear()
    }

    /**
     * 批量操作，一次性提交。
     */
    fun edit(commit: Boolean = false, action: SharedPreferences.Editor.() -> Unit) =
        sp.edit(commit = commit, action = action)
}
