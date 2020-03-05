package com.example.tingximalaya.presenters

import android.annotation.SuppressLint
import android.content.Context
import com.example.tingximalaya.data.SubscriptionDao
import com.example.tingximalaya.interfaces.ISubDaoCallBack
import com.example.tingximalaya.interfaces.ISubcriptPresenter
import com.example.tingximalaya.interfaces.ISubscriptCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers
import com.example.tingximalaya.data.Constants.MAX_SUB_COUNT



/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/27$ 9:46$
 */
@SuppressLint("StaticFieldLeak")
object SubscriptionPresenter : ISubcriptPresenter, ISubDaoCallBack {

    private var mSubscriptionDao: SubscriptionDao? = null


    fun GetContext(context: Context) {
        mSubscriptionDao = SubscriptionDao.getInstance(context)
        mSubscriptionDao?.setCallBack(this)
    }

    private var mMap: MutableMap<Long, Album> = HashMap()


    fun listSubscriptions() {
        Observable.create(ObservableOnSubscribe<Any> {
            mSubscriptionDao?.listAlbum()
        }).subscribeOn(Schedulers.io()).subscribe()
    }


    private var mCallback: ArrayList<ISubscriptCallBack> = ArrayList()

    override fun addSubScription(album: Album) {
        if (mMap.size >= MAX_SUB_COUNT) {
            //给出提示
            for (iSubscriptCallBack in mCallback) {
                iSubscriptCallBack.onSubFull()
            }
            return
        }
        Observable.create(ObservableOnSubscribe<Any> {
            mSubscriptionDao?.addAlbum(album)
        }).subscribeOn(Schedulers.io()).subscribe()

    }

    override fun deleteSubscription(album: Album) {
        Observable.create(ObservableOnSubscribe<Any> {
            mSubscriptionDao?.delAlbum(album)
        }).subscribeOn(Schedulers.io()).subscribe()

    }

    override fun getSubScriptionList() {
        listSubscriptions()
    }

    //取消
    override fun unRegistViewCallBack(t: ISubscriptCallBack) {
        mCallback.remove(t)

    }

    //注册
    override fun RegisterViewcallback(t: ISubscriptCallBack) {
        if (!mCallback.contains(t)) {
            mCallback.add(t)
        }
    }

    override fun isSub(album: Album): Boolean {
        var albums = mMap[album.id]
        return albums != null
    }

    //添加结果
    override fun addResult(isSuccess: Boolean) {
        listSubscriptions()



        for (iSubscriptCallBack in mCallback) {
            iSubscriptCallBack.onAddResult(isSuccess)
        }


    }


    //删除订阅的回调
    override fun onDelResult(isSuccess: Boolean) {
        listSubscriptions()
        for (iSubscriptCallBack in mCallback) {
            iSubscriptCallBack.onDeleteResult(isSuccess)
        }
    }

    //加载数据的回调
    override fun onSubListLoaded(album: List<Album>) {
        mMap.clear()
        for (album in album) {
            mMap[album.id] = album
        }
        for (iSubscriptCallBack in mCallback) {
            iSubscriptCallBack.onSubScriptionLoaded(album)
        }


    }


}
