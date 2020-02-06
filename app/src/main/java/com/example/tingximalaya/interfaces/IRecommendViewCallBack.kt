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
     * 加载更多
     */
    fun onLoaderMore(result: List<Album>)


    fun onRefreshMore(result: List<Album>)
}
