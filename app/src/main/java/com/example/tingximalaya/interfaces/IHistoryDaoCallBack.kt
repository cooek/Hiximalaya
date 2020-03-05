package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.track.Track


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/3/4$ 18:24$
 */
interface IHistoryDaoCallBack {


    /**
     * 添加历史结果
     */
    fun onHistoryAdd(isSuccess:Boolean)

    /**
     * 删除历史
     */
    fun onHistoryDel(isSuccess:Boolean)


    /**
     * 历史数据的结果
     */
    fun onHistoryLoaded(track:List<Track>)

    /**
     *
     * 历史内容清除
     */
    fun onHistoryClean(isSuccess:Boolean)
}
