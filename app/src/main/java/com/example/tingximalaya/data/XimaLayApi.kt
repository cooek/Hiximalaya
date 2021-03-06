package com.example.tingximalaya.data

import com.example.tingximalaya.utils.Constants
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList
import com.ximalaya.ting.android.opensdk.model.track.TrackList
import com.ximalaya.ting.android.opensdk.model.word.HotWordList
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords

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

    /**
     * 关键字搜索
     */
    fun searchByKeyword(keyword: String, page: Int, callback: IDataCallBack<SearchAlbumList>) {
        var map = HashMap<String, String>()
        map[DTransferConstants.SEARCH_KEY] = keyword
        map[DTransferConstants.PAGE] = page.toString()
        map[DTransferConstants.PLAYED_SECS] = Constants.COUNT_RECOMMEDN.toString()
        CommonRequest.getSearchedAlbums(map, callback)
    }

    /**
     * h获取推荐热词
     */
    fun HotWords(callback: IDataCallBack<HotWordList>) {
        var map = HashMap<String, String>()
        map[DTransferConstants.TOP] = Constants.CONUT_HOT_WOED.toString()
        CommonRequest.getHotWords(map, callback)
    }

    /**
     *
     *关键字联想词
     *
     */
    fun SuggestWord(keyWord: String, callback: IDataCallBack<SuggestWords>) {
        var map = HashMap<String, String>()
        map.put(DTransferConstants.SEARCH_KEY, keyWord)
        CommonRequest.getSuggestWord(map, callback)
    }

}
