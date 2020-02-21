package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/21$ 9:58$
 */
interface IAlbumDetailViewCallBack {

    /**
     * 专辑详情内容
     */
    fun onDetailListLoaded(tacks:List<Track>)


    /**
     * album
     */
    fun onAlbumLoaded(album: Album?)
}
