package com.xm.xapi.base.binding

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.xm.xapi.base.ApiActivity
import com.xm.xapi.base.ApiViewModel

/**
 * ApiBindingActivity
 *
 * @author mmxm
 * @date 2024/9/6 18:16
 */
abstract class ApiBindingActivity<T : ViewDataBinding> : ApiActivity() {

    lateinit var binding: T

    abstract fun getLayoutId(): Int

    override fun initLayout() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
        binding.lifecycleOwner = null
    }
}