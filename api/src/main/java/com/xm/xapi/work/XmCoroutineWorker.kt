package com.xm.xapi.work

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * ProxyCoroutineWorker
 * 使用[XmWorkerManager]类的startWorker方法启用
 * @author mmxm
 * @date 2023/8/21 17:43
 */
abstract class XmCoroutineWorker(
    appContext: Context,
    params: WorkerParameters
) : ListenableWorker(appContext, params) {

    companion object {
        const val TAG = "XmCoroutineWorker"

        /**
         * 默认拉取间隔, 4个小时
         * 能正常拉取到配置的情况
         */
        val DEFAULT_DELAY = TimeUnit.MINUTES.toMillis(4 * 60)

        /**
         * 最小重试间隔2分钟
         * 一般处理无网情况,或者服务器异常的情况
         */
        val MIN_DELAY = TimeUnit.MINUTES.toMillis(2)

        private val counts = hashMapOf<String, Int>()

    }

    /**
     * 任务
     * @return Boolean
     */
    abstract suspend fun doProxyWork(): Boolean

    /**
     * 是否循环
     * @return Boolean
     */
    abstract fun isLoop(): Boolean

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer ->
            MainScope().launch {
                try {
                    val workResult = doProxyWork()
                    addCount(workResult)
                    val ret = if (workResult) {
                        Result.success()
                    } else {
                        Result.failure()
                    }
                    completer.set(ret)
                } catch (e: Exception) {
                    e.printStackTrace()
                    completer.setException(e)
                } finally {
                    if (isLoop() ) {
                        val delay = getNextDelay(getCount(), getTapTime())
                        if(delay != 0L) {
                            XmWorkerManager.startWorker(
                                this@XmCoroutineWorker::class.java,
                                delay + 100
                            )//增加100ms延迟,用于上一个协程 自然结束,防止立即启动覆盖上次
                        }
                    }
                }
            }
        }
    }


    open fun getTapTime(): WorkGapTime? {
        val clsName = inputData.getString(XmWorkerManager.KEY_INPUT_DATA)
        return clsName?.let { XmWorkerManager.getWorkTime(it) }
    }

    /**
     * 获取下一次延迟时间
     * 如果isLoop为false, 此项可填0
     * @param count 成功次数
     * @return 延迟时间
     */
    open fun getNextDelay(count: Int, gapTime: WorkGapTime?): Long {
        if (gapTime == null) {
            return getMinDelay()
        }
        val remoteDelay = getDefaultDelay() //配置的延迟
        val localDelay = gapTime.getInterval()  //最后一次成功请求的间隔
        val result = gapTime.result  //最后一次请求的状态,true成功,false失败
        val delay = if (!result || localDelay > remoteDelay) {
            getMinDelay()
        } else {
            remoteDelay - localDelay
        }
        return delay
    }

    /**
     * 默认拉取间隔, 2个小时
     * 能正常拉取到配置的情况
     */
    open fun getDefaultDelay(): Long {
        return DEFAULT_DELAY
    }

    /**
     * 最小重试间隔,5分钟
     * 一般处理无网情况,或者服务器异常的情况
     */
    open fun getMinDelay(): Long {
        return MIN_DELAY
    }

    /**
     * 运行次数
     * @param workResult Boolean
     */
    open fun addCount(workResult: Boolean) {
        if (workResult) {
            inputData.getString(XmWorkerManager.KEY_INPUT_DATA)?.let {
                counts[it] = getCount() + 1
            }
        }

    }

    /**
     * 获取执行次数
     *
     * @param result true or false
     * @return 次数
     */
    open fun getCount(): Int {
        val clsName = inputData.getString(XmWorkerManager.KEY_INPUT_DATA)
        return counts[clsName] ?: 0
    }
}

