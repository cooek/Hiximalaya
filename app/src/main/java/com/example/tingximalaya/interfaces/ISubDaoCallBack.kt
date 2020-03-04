package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.album.Album


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/26$ 18:16$
 */
interface ISubDaoCallBack {


    /**
     * 添加的结果
     */
    fun addResult(isSuccess: Boolean)

    /**
     * 删除
     */

    fun onDelResult(isSuccess: Boolean)

    /**
     * 结果
     */

    fun onSubListLoaded(album: List<Album>)



}
