package com.xm.xapi.biz.kv

import android.content.Context
import android.os.Parcelable
import com.xm.xapi.XApi
import com.xm.xapi.biz.log.XLogManager
import com.xm.xapi.ktx.fromJson
import com.xm.xapi.ktx.toJson
import java.io.Serializable

/**
 * KvManager
 *
 * @author mmxm
 * @date 2024/9/10 12:13
 */
object XKvManager : IKv {

    private const val SHARED_NAME = "xapi_kv_manager"


    private val read by lazy {
        XApi.getApp().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    private val write by lazy {
        read.edit()
    }

    override fun put(key: String, value: Int) {
        write.putInt(key, value).apply()
    }

    override fun put(key: String, value: Long) {
        write.putLong(key, value).apply()
    }

    override fun put(key: String, value: Float) {
        write.putFloat(key, value).apply()
    }

    override fun put(key: String, value: Double) {
        //转string
        put(key, value.toString())
    }

    override fun put(key: String, value: Boolean) {
        write.putBoolean(key, value).apply()
    }

    override fun put(key: String, value: String) {
        write.putString(key, value).apply()
    }

    override fun put(key: String, value: ByteArray) {
        //转string
        put(key, value.toString())
    }

    override fun put(key: String, value: Parcelable) {
        //转string
        val text = value.toJson()
        if (text != null) {
            put(key, text)
        } else {
            XLogManager.e("XKvManager", "put error, value[$value] to json failed")
        }
    }

    override fun put(key: String, value: Serializable) {
        //转string
        val text = value.toJson()
        if (text != null) {
            put(key, text)
        } else {
            XLogManager.e("XKvManager", "put error, value[$value] to json failed")
        }
    }

    override fun getInt(key: String, default: Int): Int {
        return read.getInt(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return read.getLong(key, default)
    }

    override fun getFloat(key: String, default: Float): Float {
        return read.getFloat(key, default)
    }

    override fun getDouble(key: String, default: Double): Double {
        val text = getString(key, "")
        return if (text.isNotEmpty()) {
            text.toDoubleOrNull() ?: default
        } else {
            default
        }
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return read.getBoolean(key, default)
    }

    override fun getString(key: String, default: String): String {
        return read.getString(key, default) ?: default
    }

    override fun getByteArray(key: String): ByteArray? {
        val text = getString(key, "")
        return if (text.isNotEmpty()) {
            text.toByteArray()
        } else {
            null
        }
    }

    override fun <T : Parcelable> getParcelable(key: String, clazz: Class<T>): T? {
        val text = getString(key)
        return text.fromJson(clazz)
    }

    override fun <T : Serializable> getSerializable(key: String, clazz: Class<T>): T? {
        val text = getString(key)
        return text.fromJson(clazz)
    }

    override fun remove(key: String) {
        write.remove(key).apply()
    }

    override fun clean() {
        write.clear().apply()
    }
}