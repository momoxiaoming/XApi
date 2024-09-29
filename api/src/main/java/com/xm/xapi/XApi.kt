package com.xm.xapi

import android.app.Application
import android.content.Context
import com.xm.xapi.biz.lifecyle.XApiActivityMonitor

/**
 * XApi
 *
 * @author mmxm
 * @date 2024/9/10 16:56
 */
object XApi {

    private lateinit var app: Application

    fun init(context: Context) {
        app = getApplication(context)
        XApiActivityMonitor.init(app)
    }


    private fun getApplication(context: Context): Application {
        return if (context is Application) {
            context
        } else {
            val appContext = context.applicationContext
            if (appContext is Application) {
                appContext
            } else {
                appContext as Application
            }
        }
    }

    fun getApp():Application{
        return app
    }
}