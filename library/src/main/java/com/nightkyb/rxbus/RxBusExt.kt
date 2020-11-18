@file:Suppress("unused")

package com.nightkyb.rxbus

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * RxBus
 *
 * @author nightkyb created at 2019/5/28 16:43
 */

/**
 * 发送一个新的普通事件。
 */
fun <T : Any> postEvent(t: T) = RxBus.instance.post(t)

/**
 * 发送一个新的粘性事件。
 */
fun <T : Any> postStickyEvent(t: T) = RxBus.instance.postSticky(t)

/**
 * 订阅RxBus普通事件，需要手动取消订阅。
 */
inline fun <reified T : Any> RxBus.registerEvent(
    scheduler: Scheduler = AndroidSchedulers.mainThread(),
    crossinline onEvent: (T) -> Unit
): Disposable {
    return register(T::class.java, scheduler, object : RxBusObserver<T>() {
        override fun onEvent(t: T) {
            onEvent(t)
        }
    })
}

/**
 * 订阅RxBus普通事件，LifecycleOwner(Activity / Fragment) onDestroy时自动取消订阅。
 */
inline fun <reified T : Any> LifecycleOwner.registerEvent(
    scheduler: Scheduler = AndroidSchedulers.mainThread(),
    crossinline onEvent: (T) -> Unit
) {
    val disposable = RxBus.instance.register(T::class.java, scheduler, object : RxBusObserver<T>() {
        override fun onEvent(t: T) {
            onEvent(t)
        }
    })

    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    })
}

/**
 * 订阅RxBus粘性事件，需要手动取消订阅。
 */
inline fun <reified T : Any> RxBus.registerStickyEvent(
    scheduler: Scheduler = AndroidSchedulers.mainThread(),
    crossinline onEvent: (T) -> Unit
): Disposable {
    return registerSticky(T::class.java, scheduler, object : RxBusObserver<T>() {
        override fun onEvent(t: T) {
            onEvent(t)
        }
    })
}

/**
 * 订阅RxBus粘性事件，LifecycleOwner(Activity / Fragment) onDestroy时自动取消订阅。
 */
inline fun <reified T : Any> LifecycleOwner.registerStickyEvent(
    scheduler: Scheduler = AndroidSchedulers.mainThread(),
    crossinline onEvent: (T) -> Unit
) {
    val disposable =
        RxBus.instance.registerSticky(T::class.java, scheduler, object : RxBusObserver<T>() {
            override fun onEvent(t: T) {
                onEvent(t)
            }
        })

    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
    })
}
