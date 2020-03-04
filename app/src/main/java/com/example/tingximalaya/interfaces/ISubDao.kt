package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.album.Album


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/26$ 17:15$
 */
interface ISubDao {


    /**
     * 添加专家订阅
     */
    fun addAlbum(album: Album)

    /**
     * 删除订阅内容
     */
    fun delAlbum(album: Album)

    /**
     * 获取订阅内容
     */
    fun listAlbum()


    fun setCallBack(callback:ISubDaoCallBack)


}
