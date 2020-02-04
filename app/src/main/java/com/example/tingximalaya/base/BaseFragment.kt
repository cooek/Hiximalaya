package com.example.tingximalaya.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@moxfail.com$
 * @Date: 2020/2/3$ 14:49$
 */
 abstract class BaseFragment : Fragment() {

    private var mRootView: View? = null

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = onSubViewLoad(inflater,container)
        return mRootView
    }

    abstract fun onSubViewLoad(layoutInflater: LayoutInflater, container: ViewGroup?): View?


}
