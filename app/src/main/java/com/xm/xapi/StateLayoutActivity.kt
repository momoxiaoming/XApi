package com.xm.xapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.xm.xapi.base.binding.ApiBindingViewBinder
import com.xm.xapi.biz.kv.XKvManager
import com.xm.xapi.databinding.ActivityStateLayoutBinding
import com.xm.xapi.databinding.ItemSimpleListBinding
import com.xm.xapi.ktx.dp2px
import com.xm.xapi.ktx.load
import com.xm.xapi.ktx.loadCrop
import com.xm.xapi.weight.state.XStateManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

/**
 *
 *
 * @author mmxm
 * @date 2024/9/6 15:47
 */
class StateLayoutActivity : AppCompatActivity() {
    val TAG = "StateLayoutActivity"
    lateinit var binding: ActivityStateLayoutBinding
    private val adapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(String::class.java,SimpleAdapter(R.layout.item_simple_list))
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XStateManager.default()
        binding =
            DataBindingUtil.setContentView<ActivityStateLayoutBinding>(this, R.layout.activity_state_layout)

        binding.stateLayout.onRefresh {
            Log.d(TAG, "on onRefresh ")
            getData()
        }.showLoading()

        binding.simpleList.layoutManager = LinearLayoutManager(this)

        binding.simpleList.adapter=adapter

        XKvManager.put("data_list",2)



    }


    private fun getData() {
        lifecycleScope.launch {
            flow<Int> {
                delay(1000)
                emit(XKvManager.getInt("data_list",0))
            }.collectLatest {
                Log.i(TAG, "getData: $it")
                if (it == 0) {
                    //成功
                    binding.stateLayout.showContent()
                    adapter.items= mutableListOf("你好1","您好2","您好3")
                }
                if (it == 1) {
                    binding.stateLayout.showError()
                }
                if (it == 2) {
                    binding.stateLayout.showEmpty()
                }
            }
        }
    }

    class SimpleAdapter(itemViewId: Int) : ApiBindingViewBinder<String, ItemSimpleListBinding>(itemViewId) {


        override fun bindData(data: String, binding: ItemSimpleListBinding) {
            binding.itemTv.setText(data)
            binding.itemIv.load("https://iknow-pic.cdn.bcebos.com/37d12f2eb9389b5003cdf3cf9735e5dde7116e78",10.dp2px,R.mipmap.ic_launcher)
            binding.root.setOnClickListener {
                Log.d("SimpleAdapter", "bindData: onclick:$data")
            }
        }
    }
}