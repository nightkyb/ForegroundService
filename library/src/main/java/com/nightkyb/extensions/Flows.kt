@file:Suppress("unused")

package com.nightkyb.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Flows
 *
 * @author nightkyb created on 2020/6/5.
 */

/**
 * 转换为[Result]流：
 *
 * 上流发射的结果封装到[Result.success]中，
 *
 * 上流抛出的异常捕获并封装到[Result.failure]中。
 */
fun <T> Flow<T>.asResultFlow(): Flow<Result<T>> {
    return map { Result.success(it) }
        .catch { emit(Result.failure(it)) }
}
