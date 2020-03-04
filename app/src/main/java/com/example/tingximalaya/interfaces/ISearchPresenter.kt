package com.example.tingximalaya.interfaces

import com.example.tingximalaya.base.IBasePresenter
import com.ximalaya.ting.android.opensdk.model.word.HotWord

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/25$ 15:31$
 */
interface ISearchPresenter : IBasePresenter<ISearchCallBack> {


    /**
     * search
     */
    fun doSearch(keyword: String)


    /**
     * 重新搜索
     *
     */
    fun reSearch()


    /**
     * 加载更多
     */
    fun loadMore()


    /**
     * 热词
     */
    fun HotWord()


    /**
     * 获取推荐的内容
     */
    fun RecommendWord(keyword: String)

}
