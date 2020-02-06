package com.example.tingximalaya.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.tingximalaya.MainActivity
import com.example.tingximalaya.base.BaseFragment
import com.example.tingximalaya.utils.Constants
import com.example.tingximalaya.utils.FragmentCraetor

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/3$ 14:32$
 */
@Suppress("DEPRECATION")
class MainContentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): BaseFragment {
        return  FragmentCraetor().getFragment(position)
    }

    override fun getCount(): Int {
        return  Constants.INDEX_PAGE_COUNT
    }


}
