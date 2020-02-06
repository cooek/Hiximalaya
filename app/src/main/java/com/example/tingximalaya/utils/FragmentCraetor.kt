package com.example.tingximalaya.utils

import com.example.tingximalaya.base.BaseFragment
import com.example.tingximalaya.fragments.HistoryFragment
import com.example.tingximalaya.fragments.RecommendFragment
import com.example.tingximalaya.fragments.SubscritFragment

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/3$ 15:11$
 */
class FragmentCraetor {




    private var sCache: HashMap<Int, BaseFragment> = HashMap()


     fun getFragment(index: Int): BaseFragment {
        var basefragments: BaseFragment? = sCache.get(index)
        if (basefragments != null) {
            return basefragments
        }
        when (index) {
            Constants.INDEX_RECOMMENT -> basefragments = RecommendFragment()
            Constants.INDEX_SUBSCRIPT -> basefragments = SubscritFragment()
            Constants.INDEX_HISTORY -> basefragments = HistoryFragment()
        }
        sCache.put(index, basefragments!!)
        return basefragments
    }


}
