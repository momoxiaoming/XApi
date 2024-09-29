package com.xm.xapi.biz.kv

import android.os.Parcelable
import java.io.Serializable

/**
 * IKv
 *
 * @author mmxm
 * @date 2024/9/10 12:15
 */
interface IKv {
    /**
     * put and get
     *
     * key:String  value:Int
     */
    fun put(key: String, value: Int)
    fun getInt(key: String, default: Int = 0): Int

    /**
     * put and get
     *
     * key:String  value:Long
     */
    fun put(key: String, value: Long)
    fun getLong(key: String, default: Long = 0L): Long

    /**
     * put and get
     *
     * key:String  value:Float
     */
    fun put(key: String, value: Float)
    fun getFloat(key: String, default: Float = 0f): Float

    /**
     * put and get
     *
     * key:String  value:Double
     */
    fun put(key: String, value: Double)
    fun getDouble(key: String, default: Double = 0.0): Double

    /**
     * put and get
     *
     * key:String  value:Boolean
     */
    fun put(key: String, value: Boolean)
    fun getBoolean(key: String, default: Boolean = false): Boolean

    /**
     * put and get
     *
     * key:String  value:String
     */
    fun put(key: String, value: String)
    fun getString(key: String, default: String = ""): String

    /**
     * put and get
     *
     * key:String  value:ByteArray
     */
    fun put(key: String, value: ByteArray)
    fun getByteArray(key: String): ByteArray?

    /**
     * put and get
     *
     * key:String  value:Parcelable
     */
    fun put(key: String, value: Parcelable)
    fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T?

    /**
     * put and get
     *
     * key:String  value:Serializable
     */
    fun put(key: String, value: Serializable)
    fun <T : Serializable> getSerializable(key: String, clazz: Class<T>): T?

    /**
     * remove key
     */
    fun remove(key: String)

    /**
     * 清空数据
     */
    fun clean()

}