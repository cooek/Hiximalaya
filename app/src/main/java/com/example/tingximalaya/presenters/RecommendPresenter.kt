package com.example.tingximalaya.presenters

import com.example.tingximalaya.data.XimaLayApi
import com.example.tingximalaya.interfaces.IRecommendPresenter
import com.example.tingximalaya.interfaces.IRecommendViewCallBack
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxamil.com$
 * @Date: 2020/2/6$ 16:55$
 */
object RecommendPresenter : IRecommendPresenter {


    private var mCurrentRecommedTrack: List<Album>? = null

    private var malbumList: List<Album>? = null

    private var mCallback: ArrayList<IRecommendViewCallBack> = ArrayList()
    /**
     * 注册到list
     */
    override fun RegisterViewcallback(
        t: IRecommendViewCallBack
    ) {
        if (!mCallback.contains(t)) {
            mCallback.add(t)
        }
    }

    override fun unRegistViewCallBack(t: IRecommendViewCallBack) {
        mCallback.remove(t)

    }

    /**
     * 获取推荐内容
     * 猜你喜欢
     */
    override fun getRecommendList() {
        if (malbumList != null && malbumList?.size!! > 0) {
            handlerRecommendResult(malbumList!!)
            return
        }
        updateLoading()
        XimaLayApi.getRecommenList(object : IDataCallBack<GussLikeAlbumList> {
            override fun onSuccess(p0: GussLikeAlbumList?) {
                if (p0 != null) {
                    malbumList = p0.albumList

                    if (malbumList != null) {
                        handlerRecommendResult(malbumList!!)
                        Logutils.d(
                            RecommendPresenter::class.java.toString(),
                            "${p0.albumList.size}" + "个"
                        )
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {
                handlerError()
            }
        })
    }

    private fun handlerError() {
        for (iRecommendViewCallBack in mCallback) {
            iRecommendViewCallBack.onNetworkError()

        }
    }

    override fun pullRefreshMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handlerRecommendResult(albumList: List<Album>) {
        if (albumList.isEmpty()) {
            for (iRecommendViewCallBack in mCallback) {
                iRecommendViewCallBack.onEmpty()
            }
        } else {
            for (iRecommendViewCallBack in mCallback) {
                iRecommendViewCallBack.onRecommendListLoaded(albumList)
            }
            this.mCurrentRecommedTrack = albumList
        }

    }

    private fun updateLoading() {
        for (iRecommendViewCallBack in mCallback) {
            iRecommendViewCallBack.onLoading()
        }
    }

    fun CurrentTrack(): List<Album>? {
        return mCurrentRecommedTrack
    }

}
