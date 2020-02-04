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

    public val INDEX_RECOMMENT = 0

    public val INDEX_SUBSCRIPT = 1

    public val INDEX_HISTORY = 2

    public val INDEX_PAGE_COUNT = 3


    private var sCache: HashMap<Int, BaseFragment> = HashMap()


     fun getFragment(index: Int): BaseFragment {
        var basefragments: BaseFragment? = sCache.get(index)
        if (basefragments != null) {
            return basefragments
        }
        when (index) {
            INDEX_RECOMMENT -> basefragments = RecommendFragment()
            INDEX_SUBSCRIPT -> basefragments = SubscritFragment()
            INDEX_HISTORY -> basefragments = HistoryFragment()
        }
        sCache.put(index, basefragments!!)
        return basefragments
    }


}
