package com.xm.xapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.xm.xapi.databinding.ActivityGdBinding

/**
 * GodDrawableActivity
 *
 * @author mmxm
 * @date 2024/9/6 14:27
 */
class GodDrawableActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityGdBinding>(this,R.layout.activity_gd)
    }
}