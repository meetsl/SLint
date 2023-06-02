package com.zybang.slcodescanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zybang.commonlib.Logger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BuildConfig.VERSION_CODE
        BuildConfig.BUILD_TYPE
        Logger.log()
    }
}