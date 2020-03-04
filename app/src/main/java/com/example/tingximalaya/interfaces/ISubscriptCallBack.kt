package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.album.Album


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/26$ 16:47$
 */
interface ISubscriptCallBack {


    /**
     * 是否成功
     */
    fun onAddResult(isSuccess: Boolean)


    /**
     * 删除
     */
    fun onDeleteResult(isSuccess: Boolean)


    /**
     * 专家加载内容
     */
    fun onSubScriptionLoaded(album: List<Album>)




}
