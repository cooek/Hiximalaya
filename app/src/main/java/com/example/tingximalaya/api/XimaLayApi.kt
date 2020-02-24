package com.example.tingximalaya.api

import com.example.tingximalaya.presenters.AlbumDetailPresenter
import com.example.tingximalaya.utils.Constants
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList
import com.ximalaya.ting.android.opensdk.model.track.TrackList

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/24$ 18:53$
 */
object XimaLayApi {


    /**
     * 获取结果的回调
     */
    fun getRecommenList(callback: IDataCallBack<GussLikeAlbumList>) {

        var map: HashMap<String, String> = HashMap()
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMAND_COUNT.toString())
        CommonRequest.getGuessLikeAlbum(map, callback)
    }

    /**
     * 获取详情
     */

    fun getAlbumDetail(
        iDataCallBack: IDataCallBack<TrackList>,
        mCurrentAlbumId: Int,
        mCurrentPageIndex: Int
    ) {
        var map = HashMap<String, String>()
        map[DTransferConstants.ALBUM_ID] = mCurrentAlbumId.toString()
        map[DTransferConstants.SORT] = "asc"
        map[DTransferConstants.PAGE] = mCurrentPageIndex.toString()
        map[DTransferConstants.PAGE_SIZE] = Constants.RECOMMAND_COUNT.toString()
        CommonRequest.getTracks(map, iDataCallBack)
    }


}
