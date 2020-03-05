package com.example.tingximalaya.presenters

import android.content.Context
import com.example.tingximalaya.base.BaseApplication
import com.example.tingximalaya.data.Constants
import com.example.tingximalaya.data.HistoryDao

import com.example.tingximalaya.interfaces.IHistoryCallBack
import com.example.tingximalaya.interfaces.IHistoryDaoCallBack
import com.example.tingximalaya.interfaces.IHistoryPresenter
import com.ximalaya.ting.android.opensdk.model.track.Track
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/3/4$ 19:07$
 */
object HistoryPresenter : IHistoryPresenter, IHistoryDaoCallBack {

    private var mCurrentAddTrack: Track? = null
    private  var mCurremtTrack: ArrayList<Track>?=null

    private var mCallback: ArrayList<IHistoryCallBack> = ArrayList()

    private var mHistoryDao: HistoryDao? = null

    private var isDelasOutSize = false

    init {
        mHistoryDao = HistoryDao()
        mHistoryDao?.CallBack(this)
        listHistories()
    }


    //IHistoryDaoCallBack
    override fun listHistories() {
        Observable.create<Any> {
            if (mHistoryDao != null) {
                mHistoryDao?.listHistories()
            }

        }.subscribeOn(Schedulers.io()).subscribe()


    }

    override fun addHistory(track: Track) {


        if (mCurremtTrack != null && mCurremtTrack?.size!! >= Constants.MAX_HISTORY_COUNT) {
            isDelasOutSize = true
            delHistory(mCurremtTrack!![mCurremtTrack?.size!! - 1])
            this.mCurrentAddTrack = track
        } else {
            doAddHistory(track)

        }


    }

    private fun doAddHistory(track: Track) {
        Observable.create<Any> {

            if (mHistoryDao != null) {
                mHistoryDao?.addHistory(track)
            }

        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun delHistory(track: Track) {

        Observable.create<Any> {
            if (mHistoryDao != null) {
                mHistoryDao?.delHistory(track)
            }
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun cleanHistories() {
        Observable.create<Any> {
            mHistoryDao?.clearHistory()
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun unRegistViewCallBack(t: IHistoryCallBack) {
        mCallback.remove(t)
    }

    override fun RegisterViewcallback(t: IHistoryCallBack) {

        if (!mCallback.contains(t)) {
            mCallback.add(t)
        }
    }//end


    //HistoryDao 回调
    override fun onHistoryAdd(isSuccess: Boolean) {


        listHistories()

    }

    override fun onHistoryDel(isSuccess: Boolean) {
        if (isDelasOutSize && mCurrentAddTrack != null) {

            addHistory(mCurrentAddTrack!!)
            isDelasOutSize = false
        } else {
            listHistories()
        }
    }

    override fun onHistoryLoaded(track: List<Track>) {
        this.mCurremtTrack = track as ArrayList
        println("111111111111111-条目> ${track.size}")
        Observable.just(1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                for (iHistoryCallBack in mCallback) {
                    iHistoryCallBack.onHistoriesLoaded(track)
                }
            }
    }

    override fun onHistoryClean(isSuccess: Boolean) {
        listHistories()

    }//end
}
