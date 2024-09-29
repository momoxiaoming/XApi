package com.xm.xapi.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * ApiFragment
 *
 * @author mmxm
 * @date 2024/9/6 18:10
 */
abstract class ApiFragment : Fragment() {
    private var isFirst = false

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化view
     */
    protected abstract fun initView()

    /**
     * 初始化监听
     */
    protected abstract fun initObserver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
    }


    override fun onResume() {
        super.onResume()
        if (isFirst) {
            isFirst = false
            initData()
            initView()
            initObserver()
        }
    }
}