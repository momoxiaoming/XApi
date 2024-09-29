package com.xm.xapi.ktx

import android.content.Context
import android.net.ConnectivityManager

/**
 * PhoneKtx
 *
 * @author mmxm
 * @date 2024/9/6 17:15
 */
/**
 * 判断是否有网络连接
 */
fun Context.isNetConnected(): Boolean {
    val mConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val mNetworkInfo = mConnectivityManager.activeNetworkInfo
    if (mNetworkInfo != null) {
         return mNetworkInfo.isAvailable
    }
    return false
}