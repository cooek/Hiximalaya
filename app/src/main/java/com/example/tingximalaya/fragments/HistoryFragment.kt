package com.example.tingximalaya.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tingximalaya.R
import com.example.tingximalaya.base.BaseFragment

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@moxfail.com$
 * @Date: 2020/2/3$ 14:54$
 */
class HistoryFragment : BaseFragment() {


    override fun onSubViewLoad(layoutInflater: LayoutInflater, container: ViewGroup?): View? {
        return layoutInflater.inflate(R.layout.fragment_history, container,false)
    }
}
