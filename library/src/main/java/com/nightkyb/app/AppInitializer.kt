package com.nightkyb.app

import android.content.Context
import androidx.startup.Initializer
import com.nightkyb.util.SpUtils

/**
 * 应用初始化器。
 *
 * @author nightkyb created on 2020/9/25.
 */
class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        SpUtils.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(LoggerInitializer::class.java)
    }
}
