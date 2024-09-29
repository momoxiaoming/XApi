package com.xm.xapi.biz.lifecyle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.collection.arraySetOf
import androidx.lifecycle.*
import com.xm.xapi.biz.log.XLogManager

object XApiActivityMonitor {
    private const val TAG = "XApiActivityMonitor"

    /**
     * activity 生命周期回调
     */
    val callbacks: MutableList<Application.ActivityLifecycleCallbacks> = arrayListOf()

    /**
     * 最后显示的activity
     */
    private var lastResumeActivity: Activity? = null

    /**
     * 创建activity列表
     */
    private val createList = arraySetOf<Activity>()

    /**
     * 显示activity列表
     */
    private val resumeList = arraySetOf<Activity>()

    /**
     * 清栈
     */
    fun cleanTask(filter: Activity? = null) {
        createList.forEach { activity ->
            if (filter != activity) {
                activity.finish()
            }
        }
    }

    /**
     * 是否在前台
     */
    val isForeground: Boolean
        get() = foregroundLiveData.value == true

    /**
     * 获取create activity列表
     */
    fun getCreateList(): List<Activity> {
        return createList.toList()
    }

    /**
     * 获取 resume activity列表
     */
    fun getResumeList(): List<Activity> {
        return resumeList.toList()
    }

    /**
     * 获取最后显示的activity
     */
    fun getLastResumeActivity(): Activity? {
        return lastResumeActivity
    }

    /**
     * 前台状态LiveData
     */
    private val _foregroundLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val foregroundLiveData: LiveData<Boolean> = _foregroundLiveData

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ApiActivityLifecycleCallbacks())
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleChecker())
    }

    private class ApiActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            XLogManager.i(TAG, "onActivityCreated, $activity")
            createList.add(activity)
            callbacks.forEach {
                it.onActivityCreated(activity, savedInstanceState)
            }
        }

        override fun onActivityStarted(activity: Activity) {
            XLogManager.i(TAG, "onActivityStarted, $activity")
            callbacks.forEach {
                it.onActivityStarted(activity)
            }
        }

        override fun onActivityResumed(activity: Activity) {
            XLogManager.i(TAG, "onActivityResumed, $activity")
            lastResumeActivity = activity
            resumeList.add(activity)
            callbacks.forEach {
                it.onActivityResumed(activity)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            XLogManager.i(TAG, "onActivityPaused, $activity")
            callbacks.forEach {
                it.onActivityPaused(activity)
            }
        }

        override fun onActivityStopped(activity: Activity) {
            XLogManager.i(TAG, "onActivityStopped, $activity")
            if (lastResumeActivity == activity) {
                lastResumeActivity = null
            }
            resumeList.remove(activity)
            callbacks.forEach {
                it.onActivityStopped(activity)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            XLogManager.i(TAG, "onActivitySaveInstanceState, $activity")
            callbacks.forEach {
                it.onActivitySaveInstanceState(activity, outState)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            XLogManager.i(TAG, "onActivityDestroyed, $activity")
            if (lastResumeActivity == activity) {
                lastResumeActivity = null
            }
            createList.remove(activity)
            callbacks.forEach {
                it.onActivityDestroyed(activity)
            }
        }
    }

    private class LifecycleChecker : LifecycleEventObserver  {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when(event){
                Lifecycle.Event.ON_START->{
                    XLogManager.i(TAG, "onAppForeground")
                    _foregroundLiveData.value = true
                }
                Lifecycle.Event.ON_STOP->{
                    XLogManager.i(TAG, "onAppBackground")
                    _foregroundLiveData.value = false
                }
                else->{

                }
            }
        }
    }

}