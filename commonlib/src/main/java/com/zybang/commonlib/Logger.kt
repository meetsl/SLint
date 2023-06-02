package com.zybang.commonlib

import android.util.Log

/**
 * Created on 2023/5/24 5:00 PM
 * @author shilong
 *
 * desc: todo
 */
object Logger {
    fun log() {
        Log.i("Logger", BuildConfig.BUILD_TYPE)
    }
}