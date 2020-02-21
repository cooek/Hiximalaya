package com.example.tingximalaya.presenters

import com.example.tingximalaya.interfaces.IAlbumDetailPresenter
import com.example.tingximalaya.interfaces.IAlbumDetailViewCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/21$ 9:52$
 */
object AlbumDetailPresenter : IAlbumDetailPresenter {

    private var mtatgetAlbum: Album? = null

    private var mCallback: ArrayList<IAlbumDetailViewCallBack> = ArrayList()

    override fun registViewCallBack(ialbumDetailViewCallBack: IAlbumDetailViewCallBack) {
        if (!mCallback.contains(ialbumDetailViewCallBack)) {
            mCallback.add(ialbumDetailViewCallBack)
            if (mtatgetAlbum != null) {
                ialbumDetailViewCallBack.onAlbumLoaded(mtatgetAlbum)
            }
        }
    }

    override fun unregistViewCallBack(ialbumDetailViewCallBack: IAlbumDetailViewCallBack) {
        mCallback.remove(ialbumDetailViewCallBack)
    }


    override fun pullRefreshMore() {


    }

    override fun loadMore() {

    }

    override fun AlbumDetail(albumId: Int, Page: Int) {

    }

    fun TarGetAlbum(tatgetAlbum: Album) {
        this.mtatgetAlbum = tatgetAlbum
    }
}
