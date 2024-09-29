package com.xm.xapi.work

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * WorkGapTime
 * 用于记录work运行时间,以便于执行下次定时间隔
 * @author mmxm
 * @date 2023/8/22 12:19
 */
class WorkGapTime(
    @SerializedName("time")
    val time: Long,
    @SerializedName("result")
    val result: Boolean
) : Serializable {

    /**
     * 获取间隔
     */
    fun getInterval(): Long {
        return System.currentTimeMillis() - time
    }


}