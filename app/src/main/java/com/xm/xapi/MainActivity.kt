package com.xm.xapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.xm.xapi.base.ApiActivity
import com.xm.xapi.base.EmptyViewMode
import com.xm.xapi.base.binding.ApiBindingActivity
import com.xm.xapi.databinding.ActivityMainBinding
import com.xm.xapi.ktx.collectCancel
import com.xm.xapi.ktx.collectLatestCancel
import com.xm.xapi.ktx.sendEmit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random


class MainActivity : ApiBindingActivity<ActivityMainBinding>() {
    private val TAG = "MainActivity"

companion object{
    val shrpFlow= MutableSharedFlow<Int>(2)

}
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initView() {
        val flowDemo= MutableStateFlow(0)
        flowDemo.collectLatestCancel(this.lifecycleScope){

        }
        shrpFlow.collectLatestCancel(this.lifecycleScope){
            Log.d(TAG, "shrpFlow: $it")
        }


    }

    override fun initObserver() {

    }


    fun showGodDrawable(view: android.view.View) {
//        startActivity(Intent(this, GodDrawableActivity::class.java))
        shrpFlow.sendEmit(Random.nextInt(100))
    }

    fun showStateLayout(view: View) {
        startActivity(Intent(this, StateLayoutActivity::class.java))
    }
}
