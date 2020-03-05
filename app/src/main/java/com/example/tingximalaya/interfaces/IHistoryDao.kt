package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.track.Track


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/3/4$ 18:06$
 */
interface IHistoryDao {

    /**
     * 回调
     */
    fun CallBack(callback: IHistoryDaoCallBack)

    /**
     * 添加历史
     */
    fun addHistory(track: Track)

    /**
     * 删除
     */

    fun delHistory(track: Track)

    /**
     *清除内容
     */
    fun clearHistory()

    /**
     *获取历史内容
     */
    fun listHistories()

}
