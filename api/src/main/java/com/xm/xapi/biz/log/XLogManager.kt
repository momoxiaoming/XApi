package com.xm.xapi.biz.log

import android.util.Log
import com.xm.xapi.utils.MD5Util
import com.xm.xapi.utils.RefUtils

/**
 * LogManager
 *
 * @author mmxm
 * @date 2024/9/10 17:06
 */
object XLogManager {

    private var debugTag = false

    /**
     * 判断当前是否开启后门
     * fuck_door_888
     */
    private val isOpenFuckDoor by lazy {
        val propValue =RefUtils.on("android.os.SystemProperties").call<String>(null, "get", arrayOf("debug.xapi"))
        val md5 = MD5Util.MD5Encode(propValue)
        propValue != null && "695a651bdf13b731868031327ddb50ca" == md5
    }

    fun setDebug(bl: Boolean) {
        this.debugTag = bl
    }

    fun i(tag: String, msg: String) {
        printLog(0, tag, msg)
    }

    fun d(tag: String, msg: String) {
        printLog(1, tag, msg)
    }

    fun w(tag: String, msg: String) {
        printLog(2, tag, msg)
    }

    fun e(tag: String, msg: String) {
        printLog(3, tag, msg)
    }

    private fun printLog(level: Int, tag: String, msg: String) {
        if (!getDebug()) return
        when (level) {
            0 -> {
                Log.i(tag, msg)
            }

            1 -> {
                Log.d(tag, msg)
            }

            2 -> {
                Log.w(tag, msg)
            }

            3 -> {
                Log.e(tag, msg)
            }
        }
    }

    fun getDebug(): Boolean {
        return debugTag || isOpenFuckDoor
    }

}