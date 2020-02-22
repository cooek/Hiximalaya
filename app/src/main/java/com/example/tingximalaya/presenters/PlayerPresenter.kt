package com.example.tingximalaya.presenters

import com.example.tingximalaya.base.BaseAplication
import com.example.tingximalaya.interfaces.IPlayerCallBack
import com.example.tingximalaya.interfaces.lPlayerPresenter
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/22$ 10:38$
 */
object PlayerPresenter : lPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private var mCallback: ArrayList<IPlayerCallBack> = ArrayList()

    private val TAG = PlayerPresenter::class.java.toString()

    private var mTrack: Track? = null

    private var mCurrentIndex: Int = 0

    private val mXlayerManager: XmPlayerManager by lazy { XmPlayerManager.getInstance(BaseAplication().AppContext()) }

    private var isPlayListSet = false

    init {
//        this.mXlayerManager =
        //广告相关接口
        mXlayerManager.addAdsStatusListener(this)
        //注册播放器相关接口
        mXlayerManager.addPlayerStatusListener(this)
    }

    fun setPlayList(list: List<Track>, playindex: Int) {

        if (mXlayerManager != null) {
            mXlayerManager?.setPlayList(list, playindex)
            isPlayListSet = true
            mTrack = list[playindex]
            mCurrentIndex = playindex
        }


    }

    override fun play() {
        if (isPlayListSet) {
            mXlayerManager?.play()
        }
    }

    override fun pause() {
        mXlayerManager.pause()
    }

    override fun stop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun playPre() {
        mXlayerManager.playPre()

    }

    override fun playNext() {

        mXlayerManager.playNext()
    }

    override fun switchPlayMode(mode: XmPlayListControl.PlayMode) {
        println("11111111111111111111111-->${mode}")
        if (mode != null) {
            mXlayerManager.playMode = mode
        }
    }

    override fun PlayList() {
        var playlist = mXlayerManager.playList

        for (iPlayerCallBack in mCallback) {
            iPlayerCallBack.onListLoaded(playlist)
        }
    }

    override fun playByIndex(index: Int) {
        mXlayerManager.play(index)

    }

    //更新Ui
    override fun seekTo(progress: Int) {

        mXlayerManager.seekTo(progress)

    }

    override fun unRegistViewCallBack(t: IPlayerCallBack) {
        mCallback.remove(t)
    }

    override fun RegisterViewcallback(t: IPlayerCallBack) {
        if (mTrack != null) {
            t.onTrackUpdate(mTrack!!, mCurrentIndex)
        }
        if (!mCallback.contains(t)) {
            mCallback.add(t)
        }
    }

    override fun isPlay(): Boolean {
        return mXlayerManager?.isPlaying

    }


    /**
     *广告相关的回到接口
     */
    override fun onAdsStartBuffering() {

        Logutils.d(TAG, "onAdsStartBuffering....开始缓冲广告")
    }

    override fun onAdsStopBuffering() {
        Logutils.d(TAG, "onAdsStopBuffering....停止缓冲")
    }

    override fun onStartPlayAds(p0: Advertis?, p1: Int) {
        Logutils.d(TAG, "onStartPlayAds....")
    }

    override fun onStartGetAdsInfo() {
        Logutils.d(TAG, "onStartGetAdsInfo....获取广告列表")
    }

    override fun onGetAdsInfo(p0: AdvertisList?) {
        Logutils.d(TAG, "onGetAdsInfo....广告列表信息")
    }

    override fun onCompletePlayAds() {
        Logutils.d(TAG, "onCompletePlayAds....播放完成")
    }

    override fun onError(p0: Int, p1: Int) {
        Logutils.d(TAG, "onError..${p0}..错误信息${p1}")
    }
    /**
     * end
     */

    /**
     * 播放器回调
     */
    override fun onError(p0: XmPlayerException?): Boolean {
        Logutils.d(TAG, "onError....异常${p0}")
        return false
    }

    override fun onPlayStart() {
        Logutils.d(TAG, "onPlayStart....开始播放")
        for (iPlayerCallBack in mCallback) {
            iPlayerCallBack.onPlayStart()
        }
    }

    override fun onSoundSwitch(p0: PlayableModel?, p1: PlayableModel?) {
        Logutils.d(TAG, "onSoundSwitch....切歌")
        mCurrentIndex = mXlayerManager.currentIndex

        if (p1 is Track) {
            mTrack = p1 as Track
            for (iPlayerCallBack in mCallback) {
                iPlayerCallBack.onTrackUpdate(mTrack!!, mCurrentIndex)
            }
        }

        //修改
    }

    /**
     * 更新进度
     */
    override fun onPlayProgress(p0: Int, p1: Int) {
        Logutils.d(TAG, "onPlayProgress....更新进度--当前>{${p0}}${p1}")
        for (iPlayerCallBack in mCallback) {
            iPlayerCallBack.onProgressChange(p0, p1)
        }
    }

    override fun onPlayPause() {
        Logutils.d(TAG, "onPlayPause....暂停")
        for (iPlayerCallBack in mCallback) {
            iPlayerCallBack.onPlayPause()
        }
    }

    override fun onBufferProgress(p0: Int) {
        Logutils.d(TAG, "onBufferProgress....缓冲进度--当前>{${p0}}")
    }

    override fun onPlayStop() {
        for (iPlayerCallBack in mCallback) {
            iPlayerCallBack.onPlayStop()
        }
    }

    override fun onBufferingStart() {
        Logutils.d(TAG, "onBufferingStart....开始缓冲")
    }

    override fun onSoundPlayComplete() {
        Logutils.d(TAG, "onSoundPrepared....播放完成")
    }

    override fun onSoundPrepared() {
        Logutils.d(TAG, "onSoundPrepared....播放器准备完成")
    }

    override fun onBufferingStop() {
        Logutils.d(TAG, "onBufferingStop....缓冲完成")
    }
    /**
     * end
     */
}
