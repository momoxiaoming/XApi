package com.xm.xapi.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * XApiActivity
 *
 * @author mmxm
 * @date 2024/9/6 18:08
 */
abstract class ApiActivity :AppCompatActivity(),IBase {

    protected abstract fun initLayout()

    protected abstract fun initData(bundle: Bundle?)

    protected abstract fun initView()

    protected abstract fun initObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout()
        initData(savedInstanceState)
        initView()
        initObserver()
    }
}