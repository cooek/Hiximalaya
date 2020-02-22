package com.example.tingximalaya

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.SeekBar
import androidx.viewpager.widget.ViewPager
import com.example.tingximalaya.adapters.PlayerTrackPageAdapter
import com.example.tingximalaya.base.BaseActivity
import com.example.tingximalaya.interfaces.IPlayerCallBack
import com.example.tingximalaya.presenters.PlayerPresenter
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import kotlinx.android.synthetic.main.activity_pleyer.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat



/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/21$ 17:37$
 */
class PlayerActivity : BaseActivity(), IPlayerCallBack {


    private val mPlayerPresenter by lazy { PlayerPresenter }
    private val mPlayerTrackPageAdapter = PlayerTrackPageAdapter()

    private var mCurrentProgress: Int = 0

    private var mIsUserTouchProgressBar = false

    private var mviewPages: ViewPager? = null

    private var mIsUserSlidePager = false

    private var sMap: MutableMap<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> = HashMap()

    private val sPlayModeRule = HashMap<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode>()

    private var mcurrentMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST

    private val mMinFormat = SimpleDateFormat("mm:ss")

    private val mHourFormat = SimpleDateFormat("hh:mm:ss")

    //处理播放模式的切换
    //1、默认的是：PLAY_MODEL_LIST
    //2、列表循环：PLAY_MODEL_LIST_LOOP
    //3、随机播放：PLAY_MODEL_RANDOM
    //4、单曲循环：PLAY_MODEL_SINGLE_LOOP
    init {
        sPlayModeRule[XmPlayListControl.PlayMode.PLAY_MODEL_LIST] = XmPlayListControl.PlayMode. PLAY_MODEL_LIST_LOOP
        sPlayModeRule[XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP] = XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM
        sPlayModeRule[XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM] = XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP
        sPlayModeRule[XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP] = XmPlayListControl.PlayMode.PLAY_MODEL_LIST
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pleyer)
        mviewPages = findViewById(R.id.track_page_view)
        initEvent()
        mPlayerPresenter.RegisterViewcallback(this)
        mPlayerPresenter.PlayList()

        mviewPages?.adapter = mPlayerTrackPageAdapter


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEvent() {

        player_or_pause_btn.setOnClickListener {

            if (mPlayerPresenter.isPlay()) {
                mPlayerPresenter.pause()
            } else {
                mPlayerPresenter.play()
            }

        }

        //进度回调
        track_seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    mCurrentProgress = p1
                }

            }

            //触摸
            override fun onStartTrackingTouch(p0: SeekBar?) {
                mIsUserTouchProgressBar = true
            }

            //离开
            override fun onStopTrackingTouch(p0: SeekBar?) {
                mIsUserTouchProgressBar = false
                mPlayerPresenter.seekTo(mCurrentProgress)

            }
        })


        //下一首
        player_next.setOnClickListener {
            mIsUserSlidePager = false
            mPlayerPresenter.playNext()

        }

        //上一首
        player_pre.setOnClickListener {
            mIsUserSlidePager = false

            mPlayerPresenter.playPre()

        }

        //适配器


        /**
         * viewpage listenter
         */

        if (mviewPages != null) {
            mviewPages?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {


                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {

                    if (mIsUserSlidePager) {
                        mPlayerPresenter.playByIndex(position)
                    }
                    mIsUserSlidePager = false

                }
            })
            mviewPages?.setOnTouchListener { p0, p1 ->
                when (p1?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mIsUserSlidePager = true
                    }

                }
                false
            }
        }

        player_mode_switch_btn.setOnClickListener {
            var playMode = sPlayModeRule[mcurrentMode]
            if (playMode != null) {
                mPlayerPresenter.switchPlayMode(playMode!!)
                mcurrentMode = playMode!!
                updatePlayMode()

            }

        }


    }

    fun MyToas(msg: String) {
        baseContext.runOnUiThread {
            toast(msg)
        }
    }

    private fun updatePlayMode() {
        var resId = R.drawable.selector_palyer_mode_list_order
        when (mcurrentMode) {
            XmPlayListControl.PlayMode.PLAY_MODEL_LIST -> {
                resId = R.drawable.selector_palyer_mode_list_order
                MyToas("默认模式")

            }
            XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM -> {
                resId = R.drawable.selector_palyer_mode_random
                MyToas("随机模式")

            }
            XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP -> {
                resId = R.drawable.selector_palyer_mode_list_looper
                MyToas("循环模式")

            }
            XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE -> {
                resId = R.drawable.selector_palyer_mode_single_loop
                MyToas("单曲模式")

            }
        }
        player_mode_switch_btn.setImageResource(resId)

    }


    //标题
    override fun onTrackUpdate(track: Track, playindex: Int) {

        teack_title.text = track.trackTitle
        mviewPages?.setCurrentItem(playindex, true)


    }


    /**
     * mPlayerPresenter 回调接口
     */
    override fun onPlayStart() {
        //开始播放
        player_or_pause_btn.setImageResource(R.drawable.selector_palyer_stop)

    }

    override fun onPlayPause() {

        player_or_pause_btn.setImageResource(R.drawable.selector_palyer_play)

    }

    override fun onPlayStop() {
        player_or_pause_btn.setImageResource(R.drawable.selector_palyer_stop)
    }

    override fun onPlayError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nextPlay(track: Track) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPrePlay(track: Track) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListLoaded(list: List<Track>) {

        mPlayerTrackPageAdapter.setData(list)

    }

    override fun onPlayModeChange(mode: XmPlayListControl.PlayMode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //进度回调
    override fun onProgressChange(currentProgress: Int, long: Int) {
        track_seek_bar.max = long
        var totalDuration: String? = null
        var currentDuration: String? = null

        if (long > 1000 * 60 * 60) {
            totalDuration = mHourFormat.format(long)
            currentDuration = mHourFormat.format(currentProgress)
        } else {
            totalDuration = mMinFormat.format(long)
            currentDuration = mMinFormat.format(currentProgress)

        }

        if (totalDuration != null && currentDuration != null) {
            track_duraction.text = totalDuration
            current_postion.text = currentDuration
        }
        //更新进度
        //var progress = (currentProgress * 1.0f / long * 100) as Int

        if (!mIsUserTouchProgressBar) {
            track_seek_bar.progress = currentProgress
        }
    }

    override fun onAdLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAdFinished() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayerPresenter?.unRegistViewCallBack(this)
    }


}