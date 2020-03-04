package com.example.tingximalaya

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.tingximalaya.adapters.IndiocatorAdapter
import com.example.tingximalaya.adapters.MainContentAdapter
import com.example.tingximalaya.interfaces.IPlayerCallBack
import com.example.tingximalaya.presenters.PlayerPresenter
import com.example.tingximalaya.presenters.RecommendPresenter
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity


class MainActivity : FragmentActivity(), AnkoLogger, IPlayerCallBack {


    private var indicator: IndiocatorAdapter? = null


    private val mPlayerPresenter by lazy { PlayerPresenter }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        mPlayerPresenter.RegisterViewcallback(this)
        initEvent()

    }

    private fun initEvent() {
        indicator?.setOnIndicatorTapClickListener(object :
            IndiocatorAdapter.OnIndicatorTapClickListener {
            override fun onTabClick(index: Int) {
                if (content_pager != null) {
                    content_pager.setCurrentItem(index)
                }
            }

        })

        main_play_control.setOnClickListener {
            var hasPlayList = mPlayerPresenter.hasPlayLisat()
            if (!hasPlayList) {
                PlayFirstRecommend()
            } else {
                if (mPlayerPresenter.isPlaying()) {
                    main_play_control.setImageResource(R.drawable.selector_palyer_play)
                    mPlayerPresenter.pause()

                } else {
                    mPlayerPresenter.play()

                    main_play_control.setImageResource(R.drawable.selector_palyer_stop)

                }
            }

        }


        linear_control.setOnClickListener {
            var hasPlayList = mPlayerPresenter.hasPlayLisat()
            if (!hasPlayList) {
                PlayFirstRecommend()
            }
            startActivity<PlayerActivity>()
        }


        search_btn.setOnClickListener {
            startActivity<SearchActivity>()


        }


    }

    /**
     * 获取第一个条目
     */
    private fun PlayFirstRecommend() {
        var mCurrenttTrack = RecommendPresenter.CurrentTrack()
        if (mCurrenttTrack != null) {
            var album = mCurrenttTrack[0]
            var albumid = album.id
            mPlayerPresenter.playByAlbum(albumid)

        }

    }


    private fun initView() {
        //指示器相关
        main_indicator.setBackgroundColor(
            ContextCompat.getColor(this@MainActivity, R.color.mainbackcolor)
        )
        //初始化-传递参数-全局变量
        indicator = IndiocatorAdapter(this)
        val commonNavigator = CommonNavigator(this)
        //指示器适配器
        commonNavigator.adapter = indicator
        //文本自我调节宽高
        commonNavigator.isAdjustMode = true
        main_indicator.navigator = commonNavigator
        //ViewPager适配器
        content_pager.adapter = MainContentAdapter(supportFragmentManager)
        //绑定viewpager and indicator
        ViewPagerHelper.bind(main_indicator, content_pager)


    }
    //*******PlayPresenter************

    override fun onPlayStart() {
        upDatePlayControl(true)
    }


    fun upDatePlayControl(isPlayer: Boolean) {
        if (!isPlayer) {
            main_play_control.setImageResource(R.drawable.selector_palyer_play)
            mPlayerPresenter.pause()

        } else {
            mPlayerPresenter.play()

            main_play_control.setImageResource(R.drawable.selector_palyer_stop)

        }

    }

    override fun onPlayPause() {
        upDatePlayControl(false)

    }

    override fun onPlayStop() {
        upDatePlayControl(false)

    }

    override fun onPlayError() {

    }

    override fun nextPlay(track: Track) {
    }

    override fun onPrePlay(track: Track) {
    }

    override fun onListLoaded(list: List<Track>) {


    }

    override fun onPlayModeChange(mode: XmPlayListControl.PlayMode) {
    }

    override fun onProgressChange(currentProgress: Int, long: Int) {
    }

    override fun onAdLoading() {
    }

    override fun onAdFinished() {
    }

    override fun onTrackUpdate(track: Track, playindex: Int) {
        track_title.text = track.trackTitle
        track_title.isSelected = true

        track_author.text = track.announcer.nickname

        Glide.with(baseContext).load(track.coverUrlMiddle).into(track_cover_iv)

    }

    override fun updateListOrder(isReverse: Boolean) {
    }

    ///end


    override fun onDestroy() {
        super.onDestroy()
        mPlayerPresenter.unRegistViewCallBack(this)
    }


}
