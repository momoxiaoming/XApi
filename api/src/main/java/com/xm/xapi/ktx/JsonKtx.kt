package com.xm.xapi.ktx

import com.google.gson.Gson

/**
 * JsonKtx
 *
 * @author mmxm
 * @date 2024/9/10 12:07
 */

val gson = Gson()

fun Any.toJson(): String? {
    return try {
        gson.toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun <T> String.fromJson(cls: Class<T>): T? {
    return try {
        gson.fromJson<T>(this, cls)
    } catch (e: Exception) {
        null
    }
}