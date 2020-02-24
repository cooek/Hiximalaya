package com.example.tingximalaya.presenters

import android.content.Context
import com.example.tingximalaya.api.XimaLayApi
import com.example.tingximalaya.interfaces.IAlbumDetailPresenter
import com.example.tingximalaya.interfaces.IAlbumDetailViewCallBack
import com.example.tingximalaya.utils.Constants
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

    private var mCurrentPageIndex: Int = 0
    private var mCurrentAlbumId: Int = -1

    private var mtatgetAlbum: Album? = null

    private var mCallback: ArrayList<IAlbumDetailViewCallBack> = ArrayList()

    private var mTrack: ArrayList<Track> = ArrayList()


    override fun RegisterViewcallback(
        ialbumDetailViewCallBack: IAlbumDetailViewCallBack
    ) {
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
        mCurrentPageIndex++
        thatdoLoad(true)
    }


    fun thatdoLoad(isLoadMore: Boolean) {
        XimaLayApi.getAlbumDetail(object : IDataCallBack<TrackList> {
            override fun onSuccess(p0: TrackList?) {
                if (p0 != null) {
                    var tracks = p0.tracks
                    if (isLoadMore) {
                        mTrack.addAll(tracks)
                        var size = tracks.size
                        handleLoaderMoreResult(size)
                    } else {
                        mTrack.addAll(0, tracks)
                    }
                    handlerAlbumDetailResult(mTrack)
                }
            }


            override fun onError(p0: Int, p1: String?) {
                if (isLoadMore) {
                    mCurrentPageIndex--

                }
                hanlerError(p0, p1)
            }


        }, mCurrentAlbumId, mCurrentPageIndex)
    }

    private fun handleLoaderMoreResult(size: Int) {

        for (iAlbumDetailViewCallBack in mCallback) {
            iAlbumDetailViewCallBack.onLoadeMoreFinished(size)
        }


    }

    override fun AlbumDetail(albumId: Int, Page: Int?) {
        mTrack.clear()
        this.mCurrentAlbumId = albumId
        this.mCurrentPageIndex = Page!!
        thatdoLoad(false)

    }

    private fun hanlerError(p0: Int, p1: String?) {
        for (iAlbumDetailViewCallBack in mCallback) {
            iAlbumDetailViewCallBack.onNetWorkError(p0, p1)

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
