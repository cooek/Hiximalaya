package com.example.tingximalaya.presenters

import com.example.tingximalaya.interfaces.IAlbumDetailPresenter
import com.example.tingximalaya.interfaces.IAlbumDetailViewCallBack
import com.example.tingximalaya.utils.Constants
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.model.track.TrackList

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/21$ 9:52$
 */
object AlbumDetailPresenter : IAlbumDetailPresenter {

    private var mtatgetAlbum: Album? = null

    private var mCallback: ArrayList<IAlbumDetailViewCallBack> = ArrayList()

    override fun RegisterViewcallback(ialbumDetailViewCallBack: IAlbumDetailViewCallBack) {
        if (!mCallback.contains(ialbumDetailViewCallBack)) {
            mCallback.add(ialbumDetailViewCallBack)
            if (mtatgetAlbum != null) {
                ialbumDetailViewCallBack.onAlbumLoaded(mtatgetAlbum)
            }
        }
    }

    override fun unRegistViewCallBack(ialbumDetailViewCallBack: IAlbumDetailViewCallBack) {
        mCallback.remove(ialbumDetailViewCallBack)
    }


    override fun pullRefreshMore() {


    }

    override fun loadMore() {

    }

    override fun AlbumDetail(albumId: Int, Page: Int?) {

        var map = HashMap<String, String>()
        map.put(DTransferConstants.ALBUM_ID, albumId.toString())
        map.put(DTransferConstants.SORT, "asc")
        map.put(DTransferConstants.PAGE, Page.toString())
        map.put(DTransferConstants.PAGE_SIZE, Constants.RECOMMAND_COUNT.toString())
        CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
            override fun onSuccess(p0: TrackList?) {
                if (p0 != null) {
                    var tracks = p0.tracks
                    handlerAlbumDetailResult(tracks)

                    println("111111111111111111->>>>${tracks.size}")


                }
            }

            override fun onError(p0: Int, p1: String?) {
                hanlerError(p0,p1)

            }


        })
    }

    private fun hanlerError(p0: Int, p1: String?) {
        for (iAlbumDetailViewCallBack in mCallback) {
            iAlbumDetailViewCallBack.onNetWorkError(p0,p1)

        }
    }

    private fun handlerAlbumDetailResult(tracks: List<Track>) {
        for (iAlbumDetailViewCallBack in mCallback) {
            iAlbumDetailViewCallBack.onDetailListLoaded(tracks)
        }
    }


    fun TarGetAlbum(tatgetAlbum: Album) {
        this.mtatgetAlbum = tatgetAlbum
    }
}
