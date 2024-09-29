package com.xm.xapi.utils

import android.util.Log
import com.xm.xapi.ktx.catch
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 反射工具类
 *
 * @author mmxm
 * @date 2023/8/4 16:56
 */
class RefUtils {

    private val TAG = "RefUtils"
    private var targetCls: Class<*>? = null

    companion object {

        fun on(cls: Class<*>): RefUtils {
            return RefUtils().also {
                it.targetCls = cls
            }
        }

        fun on(cls: String): RefUtils {
            return RefUtils().also {
                it.targetCls = catch<Class<*>> { Class.forName(cls) }
            }
        }
    }

    /**
     * 获取变量值
     * @param obj Any?
     * @param fieldName String
     * @return T?
     */
    fun <T> get(obj: Any? = null, fieldName: String): T? {
        return getField(fieldName)?.get(obj) as? T
    }

    /**
     * 设置变量值
     * @param obj Any?
     * @param fieldName String
     * @param value Any
     */
    fun set(obj: Any? = null, fieldName: String, value: Any) {
        getField(fieldName)?.set(obj, value)
    }

    /**
     * 反射一个方法,如果改方法为静态方法,可以无需传obj
     * @param obj Any?
     * @param method String
     * @param arg Array<Any>
     * @return T?
     */
    fun <T> call(obj: Any? = null, method: String, arg: Array<Any>): T? {
        return getMethod(method, getArgClass(arg))?.invoke(obj, *arg) as? T
    }


    private fun getArgClass(array: Array<Any>): Array<Class<*>> {
        return array.map { it::class.java }.toTypedArray()
    }

    /**
     *
     * @param field String
     * @return Field?
     */
    private fun getField(field: String): Field? {
        return try {
            targetCls!!.getDeclaredField(field).also {
                it.isAccessible = true
            }
        } catch (e: Exception) {
            try {
                targetCls!!.getField(field).also {
                    it.isAccessible = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun getMethod(method: String, arg: Array<Class<*>>): Method? {
        return try {
            targetCls!!.getDeclaredMethod(method, *arg).also {
                it.isAccessible = true
            }
        } catch (e: Exception) {
            try {
                targetCls!!.getMethod(method, *arg).also {
                    it.isAccessible = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}