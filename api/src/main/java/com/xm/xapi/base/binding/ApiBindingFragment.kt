package com.xm.xapi.base.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.xm.xapi.base.ApiFragment

/**
 * ApiBindingFragment
 *
 * @author mmxm
 * @date 2024/9/9 10:25
 */
abstract class ApiBindingFragment<T: ViewDataBinding> :ApiFragment() {
    /**
     * DataBinding
     */
    protected lateinit var binding: T

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::binding.isInitialized) {
            binding.unbind()
            binding.lifecycleOwner = null
        }
    }
}