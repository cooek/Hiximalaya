package com.example.tingximalaya.presenters

import com.example.tingximalaya.interfaces.IRecommendPresenter
import com.example.tingximalaya.interfaces.IRecommendViewCallBack
import com.example.tingximalaya.utils.Constants
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
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


    private var mCallback: ArrayList<IRecommendViewCallBack> = ArrayList()
    /**
     * 注册到list
     */
    override fun RegisterViewcallback(iRecommendViewCallBack: IRecommendViewCallBack) {
        if (mCallback != null && !mCallback.contains(iRecommendViewCallBack)) {
            mCallback.add(iRecommendViewCallBack)
        }
    }

    override fun unRegistViewCallBack(iRecommendViewCallBack: IRecommendViewCallBack) {
        if (iRecommendViewCallBack != null) {
            mCallback.remove(iRecommendViewCallBack)
        }

    }

    /**
     * 获取推荐内容
     * 猜你喜欢
     */
    override fun getRecommendList() {
        updateLoading()
        var map: HashMap<String, String> = HashMap()
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMAND_COUNT.toString())

        CommonRequest.getGuessLikeAlbum(map, object : IDataCallBack<GussLikeAlbumList> {
            override fun onSuccess(p0: GussLikeAlbumList?) {
                if (p0 != null) {
                    var albumList = p0.albumList
                    if (albumList != null) {
                        handlerRecommendResult(albumList)
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
        if (mCallback != null) {
            for (iRecommendViewCallBack in mCallback) {
                iRecommendViewCallBack.onNetworkError()

            }

        }
    }

    override fun pullRefreshMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handlerRecommendResult(albumList: List<Album>) {
        if (albumList != null) {
            if (albumList.isEmpty()) {
                for (iRecommendViewCallBack in mCallback) {
                    iRecommendViewCallBack.onEmpty()
                }
            } else {
                for (iRecommendViewCallBack in mCallback) {
                    iRecommendViewCallBack.onRecommendListLoaded(albumList)

                }
            }
        }

    }

    private fun updateLoading() {
        for (iRecommendViewCallBack in mCallback) {
            iRecommendViewCallBack.onLoading()
        }
    }

}
