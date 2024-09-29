package com.xm.xapi.ktx

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop


/**
 * ViewKtx
 *
 * @author mmxm
 * @date 2024/9/6 15:03
 */

fun android.view.View.gone() {
    this.visibility = android.view.View.GONE
}

fun android.view.View.show() {
    this.visibility = android.view.View.VISIBLE
}

/**
 * 加载普通图
 * @receiver ImageView
 * @param url String
 * @param radius Int
 */
fun ImageView.load(url: String, radius: Int = 0) {
    Glide.with(this).load(url).transform(RoundedCorners(radius)).centerCrop().into(this)
}

/**
 * 加载圆角图
 * @receiver ImageView
 * @param url String
 * @param radius Int
 * @param placeholder Int
 */
fun ImageView.load(url: String, radius: Int = 0, placeholder: Int) {
    Glide.with(this).load(url).transform(RoundedCorners(radius)).placeholder(placeholder)
        .into(this)
}

/**
 * 加载圆形图
 * @receiver ImageView
 * @param url String
 * @param placeholder Int
 */
fun ImageView.loadCrop(url: String, placeholder: Int) {
    Glide.with(this).load(url).transform(CircleCrop()).placeholder(placeholder)
        .into(this)
}