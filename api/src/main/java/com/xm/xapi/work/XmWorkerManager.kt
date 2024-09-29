package com.xm.xapi.work

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.xm.xapi.XApi
import com.xm.xapi.biz.kv.XKvManager
import com.xm.xapi.biz.log.XLogManager
import com.xm.xapi.ktx.fromJson
import com.xm.xapi.ktx.toJson
import java.util.concurrent.TimeUnit

/**
 * ProxyManager
 *
 * @author mmxm
 * @date 2023/8/22 12:13
 */
object XmWorkerManager {

    private const val TAG = "XmWorkerManager"
    internal const val KEY_INPUT_DATA = "proxy:input:data"


    /**
     * 获取work运行状态与时间,
     * @param clazz Class<out ProxyCoroutineWorker>
     * @param time WorkGapTime
     */
    fun setWorkTime(clazz: Class<out XmCoroutineWorker>, result: Boolean) {
        XKvManager.put(clazz.name, WorkGapTime(System.currentTimeMillis(), result).toJson() ?: "")
    }

    /**
     * 获取work运行状态与时间
     * @param clazz Class<out ProxyCoroutineWorker>
     * @return WorkGapTime?
     */
    fun getWorkTime(clazz: Class<out XmCoroutineWorker>): WorkGapTime? {
        return try {
            val json = XKvManager.getString(clazz.name, "")
            json.fromJson(WorkGapTime::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取work运行状态与时间
     * @param clazz Class<out ProxyCoroutineWorker>
     * @return WorkGapTime?
     */
    fun getWorkTime(clazzName: String): WorkGapTime? {
        return try {
            val json = XKvManager.getString(clazzName, "")
            json.fromJson(WorkGapTime::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 启动一个协程work,
     * @param clazz T worker
     * @param delay Long 首次延时时间,单位毫秒
     * @param constraintsCallback Consumer<Builder>?
     */
    fun startWorker(
        clazz: Class<out XmCoroutineWorker>,
        delay: Long = 0,
        existingWorkPolicy: ExistingWorkPolicy = ExistingWorkPolicy.REPLACE,
        constraintsCallback: ((Constraints.Builder) -> Unit?)? = null
    ) {
        //constraints
        val constraints = Constraints.Builder().apply {
            constraintsCallback?.invoke(this)
        }
            .build()

        val clazzName = clazz.name
        XLogManager.i(TAG, "startWorker, clazzName:$clazzName delay:$delay")

        val data = Data.Builder()
            .putString(KEY_INPUT_DATA, clazzName)
            .build()

        //create task
        val request = OneTimeWorkRequest.Builder(clazz)
            .setConstraints(constraints) //设置触发条件
            .setInitialDelay(delay, TimeUnit.MILLISECONDS) //设置延迟时间
            .setInputData(data)
            .build()

        //commit task
        WorkManager.getInstance(XApi.app)
            .enqueueUniqueWork(clazzName, existingWorkPolicy, request) //设置唯一任务，保持策略
    }

    /**
     * 停止work
     * @param clazz Class<out XmCoroutineWorker>
     */
    fun stopWorker(clazz: Class<out XmCoroutineWorker>) {
        val clazzName = clazz.name
        XLogManager.i(TAG, "stopWorker, clazzName:$clazzName")
        WorkManager.getInstance(XApi.app).cancelUniqueWork(clazzName)
    }
}