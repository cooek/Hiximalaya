package com.example.tingximalaya.base

import android.app.Application
import android.content.Context
import android.os.Handler
import com.example.tingximalaya.presenters.SubscriptionPresenter
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager


/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/1/29$ 14:35$
 */
class BaseAplication : Application() {

    private var sHandler: Handler? = null

    private var mcontext: Context? = null


    override fun onCreate() {
        super.onCreate()
//        mcontext = baseContext
        val mXimalaya: CommonRequest = CommonRequest.getInstanse()
        when (DTransferConstants.isRelease) {
            true -> {
                val mAppSecret = "8646d66d6abe2efd14f2891f9fd1c8af"
                mXimalaya.setAppkey("9f9ef8f10bebeaa83e71e62f935bede8")
                mXimalaya.setPackid("com.app.test.android")
                mXimalaya.init(this, mAppSecret)
            }
            false -> {
                val mAppSecret = "0a09d7093bff3d4947a5c4da0125972e"
                mXimalaya.setAppkey("f4d8f65918d9878e1702d49a8cdf0183")
                mXimalaya.setPackid("com.ximalaya.qunfeng")
                mXimalaya.init(this, mAppSecret)
            }
        }
        /**
         * 初始化播放器
         */
        XmPlayerManager.getInstance(this).init()

        Logutils.init(this.packageName, false)
        sHandler = Handler()


        mcontext = AppContext()
    }

    fun getHandler(): Handler? {
        if (sHandler != null) {
            return sHandler
        }
        return sHandler
    }


    /**
     * 获取当前this
     */
    fun AppContext(): Context {
        return mcontext!!
    }

}
