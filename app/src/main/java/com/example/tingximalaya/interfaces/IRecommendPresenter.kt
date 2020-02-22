package com.example.tingximalaya.interfaces

import com.example.tingximalaya.base.IBasePresenter

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:cook@foxmail.com$
 * @Date: 2020/2/6$ 16:48$
 */
interface IRecommendPresenter :IBasePresenter<IRecommendViewCallBack>{
    /**
     * 获取推荐内容
     */
    fun getRecommendList()


    /**
     * 下拉刷新更多内容
     */
    fun pullRefreshMore()


    /**
     * 上拉加载更多
     */
    fun loadMore()



}
