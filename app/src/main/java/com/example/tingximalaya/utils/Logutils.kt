package com.example.tingximalaya.utils

import android.util.Log

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/1/29$ 16:10$
 */
object Logutils {

    private var sTAG: String = "LogUtil"

    //release || debug
    private var sIsRelease = false


    fun init(baseTag: String, isRelase: Boolean) {
        sTAG = baseTag
        sIsRelease = isRelase
    }


    fun d(TAG: String, content: String) {
        if (!sIsRelease) {
            Log.d("[$sTAG]$TAG", content)
        }
    }

    fun v(TAG: String, content: String) {
        if (!sIsRelease) {
            Log.d("[$sTAG]$TAG", content)
        }
    }

    fun i(TAG: String, content: String) {
        if (!sIsRelease) {
            Log.d("[$sTAG]$TAG", content)
        }
    }

    fun w(TAG: String, content: String) {
        if (!sIsRelease) {
            Log.d("[$sTAG]$TAG", content)
        }
    }

    fun e(TAG: String, content: String) {
        if (!sIsRelease) {
            Log.d("[$sTAG]$TAG", content)
        }
    }


}
