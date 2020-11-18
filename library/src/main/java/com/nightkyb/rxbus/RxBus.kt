package com.nightkyb.rxbus

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 * 基于RxJava2实现的事件总线，单例模式，支持普通事件发送和粘性事件发送，不支持背压处理。
 *
 * 要想支持背压，Observable->Flowable，Subject->FlowableProcessor，PublishSubject->PublishProcessor即可。
 *
 * 注意：在使用Sticky特性时，在不需要某Sticky事件时，通过removeStickyEvent(Class<T> eventType)移除它，
 * 最保险的做法是：在主Activity的onDestroy里removeAllStickyEvents()。
 *
 * @author nightkyb created at 2016/7/29 10:22
 */
class RxBus private constructor(
    private val subject: Subject<Any>,
    private val stickyEventMap: MutableMap<Class<*>, Any>
) {
    companion object {
        /**
         * 线程安全的单例RxBus，内部实现了双重校验锁式(Double Check Lock)的单例模式。
         *
         * Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，要避免该问题，需要将Subject
         * 转换为一个SerializedSubject，类中把线程非安全的PublishSubject包装成线程安全的SerializedSubject。
         *
         * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者。
         */
        @JvmStatic
        val instance: RxBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RxBus(
                subject = PublishSubject.create<Any>().toSerialized(),
                stickyEventMap = ConcurrentHashMap()
            )
        }
    }

    /**
     * 发送一个新的普通事件。
     */
    fun post(event: Any) = subject.onNext(event)

    /**
     * 发送一个新的粘性事件。
     */
    fun postSticky(event: Any) {
        synchronized(stickyEventMap) {
            stickyEventMap.put(event.javaClass, event)
        }
        post(event)
    }

    /**
     * 注册普通事件，根据传递的eventType事件类型返回特定类型(eventType)的被观察者。
     * ofType操作符只发射指定类型的数据，其内部就是filter + cast。
     */
    private fun <T> register(eventType: Class<T>): Observable<T> = subject.ofType(eventType)

    /**
     * 返回一个普通事件的订阅者Disposable，Disposable可以用来取消订阅。
     */
    @JvmOverloads
    fun <T> register(
        type: Class<T>,
        scheduler: Scheduler = AndroidSchedulers.mainThread(),
        observer: RxBusObserver<T>
    ): Disposable = register(type).observeOn(scheduler).subscribeWith(observer)

    /**
     * 注册粘性事件，根据传递的eventType类型返回特定类型(eventType)的被观察者。
     */
    private fun <T> registerSticky(eventType: Class<T>): Observable<T> =
        synchronized(stickyEventMap) {
            val observable = subject.ofType(eventType)
            val event = stickyEventMap[eventType]

            return if (event != null) {
                observable.mergeWith(Observable.just(eventType.cast(event)))
            } else {
                observable
            }
        }

    /**
     * 返回一个粘性事件的订阅者Disposable，Disposable可以用来取消订阅。
     */
    @JvmOverloads
    fun <T> registerSticky(
        type: Class<T>,
        scheduler: Scheduler = AndroidSchedulers.mainThread(),
        observer: RxBusObserver<T>
    ): Disposable = registerSticky(type).observeOn(scheduler).subscribeWith(observer)

    /**
     * 根据eventType获取Sticky事件。
     */
    fun <T> getStickyEvent(eventType: Class<T>): T? = synchronized(stickyEventMap) {
        return eventType.cast(stickyEventMap[eventType])
    }

    /**
     * 移除指定eventType的Sticky事件。
     */
    fun <T> removeStickyEvent(eventType: Class<T>) = synchronized(stickyEventMap) {
        stickyEventMap.remove(eventType)
    }

    /**
     * 移除所有的Sticky事件。
     */
    fun removeAllStickyEvents() = synchronized(stickyEventMap) {
        stickyEventMap.clear()
    }

    /**
     * 判断是否有订阅者。
     */
    fun hasObservers(): Boolean = subject.hasObservers()
}
