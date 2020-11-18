package com.nightkyb.rxbus

import io.reactivex.observers.DisposableObserver

/**
 * 为RxBus使用的Observer，主要提供onNext事件的try-catch，当出现异常时，不会导致事件总线终止。
 *
 * @author nightkyb created at 2016/7/29 10:25
 */
abstract class RxBusObserver<T> : DisposableObserver<T>() {

    override fun onNext(t: T) {
        try {
            onEvent(t)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onComplete() {}

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    protected abstract fun onEvent(t: T)
}
