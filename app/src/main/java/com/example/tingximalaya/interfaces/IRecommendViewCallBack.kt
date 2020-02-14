package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.album.Album


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/6$ 16:51$
 */
interface IRecommendViewCallBack {

    /**
     * 获取推荐内容
     */

    fun onRecommendListLoaded(reuslt: List<Album>)

    /**
     * 网络错误
     */
    fun onNetworkError()

    /**
     * 数据为空
     */
    fun onEmpty()

    /**
     * 正在加载
     */
    fun onLoading()

}
