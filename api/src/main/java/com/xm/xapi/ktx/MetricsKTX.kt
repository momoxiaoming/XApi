package com.xm.xapi.ktx

import android.content.Context
import android.content.res.Resources

/**
 * 屏幕尺寸相关
 *
 * @author mmxm
 * @date 2024/9/6 14:18
 */

val Float.dp2px: Float
    get() = this * (Resources.getSystem().displayMetrics.density)

val Float.px2dp: Float
    get() = this / (Resources.getSystem().displayMetrics.density)

val Int.dp2px: Int
    get() = (this * (Resources.getSystem().displayMetrics.density)).toInt()

val Int.px2dp: Int
    get() = (this / (Resources.getSystem().displayMetrics.density)).toInt()

/**
 * 获取屏幕宽度
 */
val Context.screenWidth:Int
    get() = getScreenWidthAndHeight(this).first

/**
 * 获取屏幕高度
 */
val Context.screenHeight:Int
    get() = getScreenWidthAndHeight(this).second

/**
 * 获取屏幕高和宽
 * @param context Context
 * @return Pair<Int, Int>
 */
fun getScreenWidthAndHeight(context: Context): Pair<Int, Int> {
    val displayMetrics = context.resources.displayMetrics
    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels
    return Pair(width, height)
}