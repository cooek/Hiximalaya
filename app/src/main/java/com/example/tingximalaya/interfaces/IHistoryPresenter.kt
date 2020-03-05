package com.example.tingximalaya.interfaces

import com.example.tingximalaya.base.IBasePresenter
import com.ximalaya.ting.android.opensdk.model.track.Track

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/3/4$ 18:13$
 */
interface IHistoryPresenter : IBasePresenter<IHistoryCallBack> {


    /**
     * 获取历史内容
     */
    fun listHistories()


    /**
     * 添加
     */
    fun addHistory(track: Track)

    /**
     * 删除
     */
    fun delHistory(track: Track)

    /**
     * 清除历史
     */
    fun cleanHistories()


}
