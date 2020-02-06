package com.example.tingximalaya.interfaces

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:cook@foxmail.com$
 * @Date: 2020/2/6$ 16:48$
 */
interface IRecommendPresenter {
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

    /**
     * 回调接口引用
     */
    fun RegisterViewcallback(iRecommendViewCallBack:IRecommendViewCallBack)


    /**
     * 取消 制空引用
     */
    fun unRegisterViewcallback(iRecommendViewCallBack:IRecommendViewCallBack)


}
