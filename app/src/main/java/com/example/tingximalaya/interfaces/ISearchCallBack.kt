package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.word.HotWord
import com.ximalaya.ting.android.opensdk.model.word.QueryResult


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/25$ 15:32$
 */
interface ISearchCallBack {

    /**
     * 结果回调
     */
    fun onSearchResultLoaded(result: List<Album>)


    /**
     * 获取推荐热词
     */
    fun onHotWordLoaded(hotWordList: List<HotWord>)


    /**
     * 加载更多
     *
     */
    fun inLoadMoreResult(result: List<Album>, isOkay: Boolean)


    fun onRecommendWordLoaded(keyWorkList: List<QueryResult>)

    /**
     * 错误
     */
    fun onError(p0: Int, p1: String?)

}
