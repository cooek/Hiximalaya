package com.example.tingximalaya.presenters

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.tingximalaya.data.XimaLayApi
import com.example.tingximalaya.base.BaseAplication
import com.example.tingximalaya.interfaces.IPlayerCallBack
import com.example.tingximalaya.interfaces.lPlayerPresenter
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.model.track.TrackList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import java.util.*
import kotlin.collections.ArrayList

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/22$ 10:38$
 */
@SuppressLint("StaticFieldLeak")
object PlayerPresenter : lPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {


    private var mProgressDuration: Int = 0
    private var mCurrentProgressPosition: Int = 0
    private var mCallback: ArrayList<IPlayerCallBack> = ArrayList()

    private val TAG = PlayerPresenter::class.java.toString()

    private var mTrack: Track? = null

    private var mCurrentIndex: Int = 0

    private var DEFAULT_PLAY_INDEX = 0

    private val mXlayerManager: XmPlayerManager by lazy { XmPlayerManager.getInstance(BaseAplication().AppContext()) }

    private var isPlayListSet = false

    private var misReverse = false

    private var mcurrentMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST

    //PLAY_MODEL_LIST
    //PLAY_MODEL_LIST_LOOP
    //PLAY_MODEL_RANDOM
    //PLAY_MODEL_SINGLE_LOOP

    public val PLAY_MODEL_LIST_INT: Int = 0
    public val PLAY_MODEL_LIST_LOOP_INT: Int = 1
    public val PLAY_MODEL_RANDOM_INT: Int = 2
    public val PLAY_MODEL_SINGLE_LOOP_INT: Int = 3

    public val PLAY_MODE_SP_NAME = "PlayMode"
    public val PLAY_MODE_SP_KEY = "currentPlayMode"

    private var playModeSP: SharedPreferences? = null

    private var mContext: Context? = null

    init {
        //广告相关接口
        mXlayerManager.addAdsStatusListener(this)
        //注册播放器相关接口
        mXlayerManager.addPlayerStatusListener(this)

    }

    fun getcontext(
        baseContext: Context
    ) {
        mContext = baseContext
        playModeSP = mContext?.getSharedPreferences(PLAY_MODE_SP_NAME, Context.MODE_PRIVATE)


    }

    fun setPlayList(
        list: List<Track>,
        playindex: Int
    ) {
        mXlayerManager.setPlayList(list, playindex)
        isPlayListSet = true
        mTrack = list[playindex]
        mCurrentIndex = playindex


    }

    override fun play() {
        if (isPlayListSet) {
            this.mXlayerManager.play()
        }
    }

    override fun pause() {
        mXlayerManager.pause()
    }

    override fun stop() {
    }

    override fun playPre() {
        mXlayerManager.playPre()

    }

    override fun playNext() {

        mXlayerManager.playNext()
    }

    override fun switchPlayMode(mode: XmPlayListControl.PlayMode) {
        mXlayerManager.playMode = mode
        mcurrentMode = mode
        for (iPlayerCallBack in mCallback) {
            iPlayerCallBack.onPlayModeChange(mode)
        }
        var edit = playModeSP?.edit()
        edit?.putInt(PLAY_MODE_SP_KEY, getIntByPlayMode(mode))
        edit?.commit()


    }

    fun getIntByPlayMode(mode: XmPlayListControl.PlayMode): Int {

        when (mode) {
            XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP -> {
                return this.PLAY_MODEL_SINGLE_LOOP_INT
            }
            XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE -> {
                return this.PLAY_MODEL_LIST_LOOP_INT
            }
            XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM -> {
                return this.PLAY_MODEL_RANDOM_INT
            }
            XmPlayListControl.PlayMode.PLAY_MODEL_LIST -> {
                return this.PLAY_MODEL_LIST_INT
            }
        }
        return PLAY_MODEL_LIST_INT
    }

    fun getIntByInt(intdex: Int): XmPlayListControl.PlayMode {

        when (intdex) {
            PLAY_MODEL_SINGLE_LOOP_INT -> {
                return XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP
            }


            PLAY_MODEL_LIST_LOOP_INT -> {
                return XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE
            }

            PLAY_MODEL_RANDOM_INT -> {
                return XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM
            }

            PLAY_MODEL_LIST_INT -> {
                return XmPlayListControl.PlayMode.PLAY_MODEL_LIST
            }
        }
        return XmPlayListControl.PlayMode.PLAY_MODEL_LIST
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

    override fun RegisterViewcallback(
        t: IPlayerCallBack
    ) {

        if (mTrack != null) {
            t.onTrackUpdate(mTrack!!, mCurrentIndex)
            t.onProgressChange(mCurrentProgressPosition, mProgressDuration)
            handlePlayState(t)
        }
        if (!mCallback.contains(t)) {
            mCallback.add(t)
        }
        var current = playModeSP?.getInt(PLAY_MODE_SP_KEY, PLAY_MODEL_LIST_INT)
        if (current != null) {
            mcurrentMode = getIntByInt(current!!)
        }

        t.onPlayModeChange(mcurrentMode)


    }

    private fun handlePlayState(t: IPlayerCallBack) {
        var playerststus = mXlayerManager.playerStatus

        if (PlayerConstants.STATE_STARTED == playerststus) {
            t.onPlayStart()
        } else {
            t.onPlayPause()
        }
    }

    override fun isPlaying(): Boolean {
        return mXlayerManager.isPlaying

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
        this.mCurrentProgressPosition = p0
        this.mProgressDuration = p1
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
        mXlayerManager.playMode = mcurrentMode
        if (mXlayerManager.playerStatus == PlayerConstants.STATE_PREPARED) {
            mXlayerManager.play()
        }

    }

    override fun onBufferingStop() {
        Logutils.d(TAG, "onBufferingStop....缓冲完成")
    }

    override fun reversePlayList() {
        var palyList: List<Track> = mXlayerManager.playList

        mCurrentIndex = palyList.size - 1 - mCurrentIndex
        Collections.reverse(palyList)
        misReverse = !misReverse
        mXlayerManager.setPlayList(palyList, mCurrentIndex)

        mTrack = mXlayerManager.currSound as Track


        for (iPlayerCallBack in mCallback) {
            iPlayerCallBack.onListLoaded(palyList)
            iPlayerCallBack.onTrackUpdate(mTrack!!, mCurrentIndex)
            iPlayerCallBack.updateListOrder(misReverse)
        }
    }

    fun hasPlayLisat(): Boolean {
        return isPlayListSet
    }

    override fun playByAlbum(id: Long) {

        var currentid = id
        XimaLayApi.getAlbumDetail(object : IDataCallBack<TrackList> {
            override fun onSuccess(p0: TrackList?) {
                var track = p0?.tracks
                if (track != null) {
                    mXlayerManager.setPlayList(track, DEFAULT_PLAY_INDEX)
                    isPlayListSet = true
                    mTrack = track[DEFAULT_PLAY_INDEX]
                    mCurrentIndex = DEFAULT_PLAY_INDEX
                }

            }

            override fun onError(p0: Int, p1: String?) {

            }
        }, currentid.toInt(), 1)
    }

    /**
     * end
     */
}
