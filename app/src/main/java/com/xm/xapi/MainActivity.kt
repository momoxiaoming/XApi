package com.xm.xapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.xm.xapi.base.ApiActivity
import com.xm.xapi.base.EmptyViewMode
import com.xm.xapi.base.binding.ApiBindingActivity
import com.xm.xapi.databinding.ActivityMainBinding


class MainActivity : ApiBindingActivity<ActivityMainBinding>() {
    private val TAG = "MainActivity"
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initView() {

    }

    override fun initObserver() {

    }


    fun showGodDrawable(view: android.view.View) {
        startActivity(Intent(this, GodDrawableActivity::class.java))
    }

    fun showStateLayout(view: View) {
        startActivity(Intent(this, StateLayoutActivity::class.java))
    }
}
