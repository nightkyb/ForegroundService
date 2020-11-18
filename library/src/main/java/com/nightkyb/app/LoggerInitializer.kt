package com.nightkyb.app

import android.content.Context
import androidx.startup.Initializer
import com.nightkyb.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * 日志打印初始化器。
 *
 * @author nightkyb created on 2020/9/25.
 */
class LoggerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val prettyFormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)      // (Optional) Whether to show thread info or not. Default true
            .methodCount(1)            // (Optional) How many method line to show. Default 2
            .methodOffset(0)           // (Optional) Hides internal method calls up to offset. Default 5
            // .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
            // .tag("My custom tag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(prettyFormatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
