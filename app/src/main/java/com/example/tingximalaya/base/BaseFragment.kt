package com.example.tingximalaya.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast


/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@moxfail.com$
 * @Date: 2020/2/3$ 14:49$
 */
open abstract class BaseFragment : Fragment(), AnkoLogger {
    private var mRootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return onSubViewLoad(inflater, container)
    }



    abstract fun onSubViewLoad(layoutInflater: LayoutInflater, container: ViewGroup?): View?

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }
    open fun initView() {
    }

    open fun MyToas(msg: String) {
        context?.runOnUiThread { toast(msg) }
    }

}
