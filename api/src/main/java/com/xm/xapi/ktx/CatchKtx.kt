package com.xm.xapi.ktx

/**
 * CatchKtx
 *
 * @author mmxm
 * @date 2024/9/6 17:27
 */

fun <T> catch(block: () -> T): T? {
    return try {
        block.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}