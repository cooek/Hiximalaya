package com.example.tingximalaya.presenters

import com.example.tingximalaya.data.XimaLayApi
import com.example.tingximalaya.interfaces.ISearchCallBack
import com.example.tingximalaya.interfaces.ISearchPresenter
import com.example.tingximalaya.utils.Constants
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList
import com.ximalaya.ting.android.opensdk.model.word.HotWordList
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords


/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/25$ 15:54$
 */
object SearchPresenter : ISearchPresenter {


    //当前的搜索关键字
    private var mCurrentKeyword: String? = null

    private var DEFAULT_PAGE = 1

    private var mCurrentPage = DEFAULT_PAGE

    private var mCallback: ArrayList<ISearchCallBack> = ArrayList()

    private var mIsLoadMore = false

    private var mSearchResult: ArrayList<Album> = ArrayList()

    override fun doSearch(keyword: String) {
        mCurrentPage = DEFAULT_PAGE
        mSearchResult.clear()
        this.mCurrentKeyword = keyword
        Search(keyword)
    }

    private fun Search(keyword: String) {
        XimaLayApi.searchByKeyword(keyword, mCurrentPage, object : IDataCallBack<SearchAlbumList> {
            override fun onSuccess(p0: SearchAlbumList?) {
                var album = p0?.albums

                mSearchResult.addAll(album!!)
                if (mIsLoadMore) {
                    for (iSearchCallBack in mCallback) {
                        iSearchCallBack.inLoadMoreResult(mSearchResult, album.size != 0)
                    }
                    mIsLoadMore = false
                } else {
                    for (iSearchCallBack in mCallback) {
                        iSearchCallBack.onSearchResultLoaded(mSearchResult)

                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {

                for (iSearchCallBack in mCallback) {
                    if (mIsLoadMore) {
                        iSearchCallBack.inLoadMoreResult(mSearchResult, false)
                        mCurrentPage--
                        mIsLoadMore = false


                    } else {
                        iSearchCallBack.onError(p0, p1)
                    }
                }


            }

        })
    }

    override fun reSearch() {
        if (mCurrentKeyword != null) {
            Search(mCurrentKeyword!!)
        }

    }


    override fun loadMore() {

        if (mSearchResult.size < Constants.COUNT_RECOMMEDN) {
            for (iSearchCallBack in mCallback) {
                iSearchCallBack.inLoadMoreResult(mSearchResult, false)
            }
        } else {
            mIsLoadMore = true
            mCurrentPage++
            Search(mCurrentKeyword!!)
        }

    }


    override fun HotWord() {

        XimaLayApi.HotWords(object : IDataCallBack<HotWordList> {
            override fun onSuccess(p0: HotWordList?) {
                if (p0 != null) {
                    var list = p0.hotWordList
                    for (iSearchCallBack in mCallback) {
                        iSearchCallBack.onHotWordLoaded(list)
                    }
                }

            }

            override fun onError(p0: Int, p1: String?) {


            }
        })
    }

    override fun RecommendWord(keyword: String) {
        XimaLayApi.SuggestWord(keyword, object : IDataCallBack<SuggestWords> {
            override fun onSuccess(p0: SuggestWords?) {
                if (p0 != null) {
                    var keyWordList = p0.keyWordList
                    for (iSearchCallBack in mCallback) {
                        iSearchCallBack.onRecommendWordLoaded(keyWordList)
                    }
                }
            }

            override fun onError(p0: Int, p1: String?) {
                for (iSearchCallBack in mCallback) {
                    iSearchCallBack.onError(p0, p1)
                }
            }
        })
    }

    override fun unRegistViewCallBack(t: ISearchCallBack) {
        mCallback.remove(t)
    }

    override fun RegisterViewcallback(t: ISearchCallBack) {

        if (!mCallback.contains(t)) {
            mCallback.add(t)
        }

    }


}
