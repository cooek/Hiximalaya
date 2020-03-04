package com.example.tingximalaya.interfaces

import com.example.tingximalaya.base.IBasePresenter
import com.ximalaya.ting.android.opensdk.model.album.Album


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/26$ 16:46$
 */

interface ISubcriptPresenter : IBasePresenter<ISubscriptCallBack> {

    /// 订阅不能超过一百个

    /**
     * 添加订阅
     */
    fun addSubScription(album: Album)


    /**
     * 删除
     */
    fun deleteSubscription(album: Album)

    /**
     * 获取订阅的列表
     */
    fun getSubScriptionList()


    /**
     * 判断当前专辑是否收藏
     */

    fun isSub(album: Album):Boolean

}
