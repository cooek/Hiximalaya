package com.example.tingximalaya.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.tingximalaya.R
import kotlinx.android.synthetic.main.fragment_error_view.view.*


/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/10$ 9:56$
 */
abstract class UILoder : FrameLayout {
    private var RetryClikListeners: onRetryClikListener? = null

    private var mLoadingView: View? = null
    private var mSuccessView: View? = null
    private var mNetWorkError: View? = null
    private var mEmptView: View? = null


    enum class UlStatus {
        LoADING, SUCCESS, NETWORK_ERROR, EMPTY, NONE
    }

    var mCurrentStstus: UlStatus = UlStatus.NONE


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inits()
    }

    /**
     * 更新
     */
    fun updateStatus(ststus: UlStatus) {
        this.mCurrentStstus = ststus
        switchUIByCurrentStstus()
        println("running from lambda: ${Thread.currentThread()}")
    }

    private fun inits() {
        switchUIByCurrentStstus()

    }

    private fun switchUIByCurrentStstus() {
        //加载中
        if (mLoadingView == null) {
            mLoadingView = getLoadingView()
            addView(mLoadingView)
        }
        println("测试！！！1")

        mLoadingView?.visibility = when (mCurrentStstus == UlStatus.LoADING) {
            true -> View.VISIBLE
            false -> View.GONE
        }


        //成功
        if (mSuccessView == null) {
            mSuccessView = getSuccessView(this)
            addView(mSuccessView)
        }

        mSuccessView?.visibility = when (mCurrentStstus == UlStatus.SUCCESS) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        println("测试！！！3")

        //错误
        if (mNetWorkError == null) {
            mNetWorkError = getNetWorkErrorView()
            addView(mNetWorkError)
            println("测试！！！2")

        }
        mNetWorkError?.visibility =
            when (mCurrentStstus == UlStatus.NETWORK_ERROR) {
                true -> View.VISIBLE
                false -> View.GONE
            }

        //数据为空
        if (mEmptView == null) {
            mEmptView = getEmptView()
            addView(mEmptView)
        }
        mEmptView?.visibility = when (mCurrentStstus == UlStatus.EMPTY) {
            true -> View.VISIBLE
            false -> View.GONE
        }


    }

    private fun getEmptView(): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_empty_view, this, false)
    }

    private fun getNetWorkErrorView(): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_error_view, this, false)
            .also {
                it.findViewById<LinearLayout>(R.id.network_error_icon).setOnClickListener {
                    if (RetryClikListeners != null) {
                        RetryClikListeners?.onRetryClick()
                    }
                }
            }
    }

    abstract fun getSuccessView(container: ViewGroup?): View?

    private fun getLoadingView(): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_loading_view, this, false)

    }

    fun onRetryClikListeners(onretryClikListener: onRetryClikListener) {
        this.RetryClikListeners = onretryClikListener
    }

    interface onRetryClikListener {
        fun onRetryClick()
    }

}
