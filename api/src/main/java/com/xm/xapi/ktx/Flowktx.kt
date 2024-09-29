package com.xm.xapi.ktx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * FlowHelper
 *
 * @author mmxm
 * @date 2024/6/28 15:11
 */

/**
 * stateflow 转可变 MutableStateFlow
 * @receiver StateFlow<T>
 * @param scope CoroutineScope
 * @return MutableStateFlow<T>
 */
fun <T> StateFlow<T>.toMutableStateFlow(scope: CoroutineScope): MutableStateFlow<T> {
    val mutableSharedFlow = MutableStateFlow<T>(this.value)
    scope.launch {
        this@toMutableStateFlow.collect { value ->
            mutableSharedFlow.emit(value)
        }
    }

    return mutableSharedFlow
}
/**
 * liveData转stateflow
 * @receiver LiveData<T>
 * @param scope CoroutineScope
 * @return StateFlow<T>
 */
 fun <T> LiveData<T>.asStateFlow(scope: CoroutineScope): StateFlow<T?> {
    return this.asFlow().stateIn(scope, SharingStarted.Lazily, this.value)
}

/**
 * 启动一个自定义范围协程,默认是default线程
 * @param context CoroutineContext
 * @param start CoroutineStart
 * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<CoroutineScope, Unit>
 */
fun launch(
    context: CoroutineContext = Dispatchers.Default,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return CoroutineScope(context).launch(context, start, block)
}

/**
 * 自带协程collect
 * @receiver MutableStateFlow<T>
 * @param block Function1<T, Any> 当返回值是true时,会取消掉当前协程,不再监听
 */
fun <T> Flow<T>.collectLatestCancel(
    scope: CoroutineScope = CoroutineScope(
        Dispatchers.Default
    ), block: (T) -> Any
):Job {
   return scope.launch {
        this@collectLatestCancel.collectLatest {
            val ret = block.invoke(it)
            if (ret == true) {
                cancel()
            }
        }
    }
}

/**
 * 自带协程collect
 * flow的一些特殊用法
 * debounce 范围时间内只响应最新值
 * drop(1)  只响应计数后的值
 * @receiver MutableStateFlow<T>
 * @param block Function1<T, Any> 当返回值是true时,会取消掉当前协程,不再监听
 */
fun <T> Flow<T>.collectCancel(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    block: (T) -> Any)
:Job{
   return scope.launch {
        this@collectCancel.collect { it ->
            val ret = block.invoke(it)
            if (ret == true) {
                cancel()
            }
        }
    }
}

/**
 * flow发送并更新值
 * @receiver MutableStateFlow<T>
 * @param t T
 */
fun <T> FlowCollector<T>.sendEmit(t: T,context: CoroutineContext = Dispatchers.Default,
):Job {
     return launch(context){
        this@sendEmit.emit(t)
    }
}




