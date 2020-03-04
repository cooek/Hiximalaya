package com.example

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tingximalaya.PlayerActivity
import com.example.tingximalaya.R
import com.example.tingximalaya.adapters.DetailListAdapter
import com.example.tingximalaya.base.BaseActivity
import com.example.tingximalaya.interfaces.IAlbumDetailViewCallBack
import com.example.tingximalaya.interfaces.IPlayerCallBack
import com.example.tingximalaya.interfaces.ISubscriptCallBack
import com.example.tingximalaya.presenters.AlbumDetailPresenter
import com.example.tingximalaya.presenters.PlayerPresenter
import com.example.tingximalaya.presenters.SubscriptionPresenter
import com.example.tingximalaya.utils.ImageBlur
import com.example.tingximalaya.views.UILoder
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.runBlocking
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooekU@foxmail.com$
 * @Date: 2020/2/21$ 9:14$
 */
class DetailActivity : BaseActivity(), IAlbumDetailViewCallBack, UILoder.onRetryClikListener,
    DetailListAdapter.ItemCilckListener, IPlayerCallBack, ISubscriptCallBack {


    private var mCurrentAlbum: Album? = null
    private var mTrackTitle: String? = null
    private var mCurrentPage = 1

    private var mCurrentId = -1

    private var isLoadMore = false

    private var DEFAULT_PLAY_INDEX = 0

    private var mCurrentTrack: List<Track>? = null


    private var mdetailListAdapter: DetailListAdapter? = null

    private val mPlayerPresenter by lazy { PlayerPresenter }

    private val mSubscriptionPresenter = SubscriptionPresenter

    private var muiLoader: UILoder? = null

    private var mLlinearLayoutManager: LinearLayoutManager? = null

    private val mAlbumDetailPresenter by lazy { AlbumDetailPresenter }

    private var mrefress_layout: TwinklingRefreshLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.statusBarColor = Color.TRANSPARENT
        mSubscriptionPresenter.GetContext(this)
        mSubscriptionPresenter.getSubScriptionList()
        initPresenter()
        updatestate()
        updatdePlaySate(mPlayerPresenter.isPlaying())
        if (muiLoader == null) {
            muiLoader = object : UILoder(this) {
                override fun getSuccessView(container: ViewGroup?): View? {
                    return createSuccessView(container)
                }

            }
            detaill_list_fragme.removeAllViews()
            detaill_list_fragme.addView(muiLoader)
            muiLoader?.onRetryClikListeners(this)

        }
        initEistener()

    }

    private fun updatestate() {
        if (mCurrentAlbum != null) {
            var isSub = mSubscriptionPresenter.isSub(mCurrentAlbum!!)
            detail_sub_btn.text = when (!isSub) {
                true -> {
                    "+ 订阅"
                }
                false -> {
                    "取消订阅"
                }

            }

        }

    }

    private fun initPresenter() {
        mAlbumDetailPresenter.RegisterViewcallback(this)
        mPlayerPresenter.RegisterViewcallback(this)
        mSubscriptionPresenter.RegisterViewcallback(this)
    }


    private fun initEistener() {
        detail_play_control.setOnClickListener {

            var isHasPlayList = mPlayerPresenter.hasPlayLisat()
            if (isHasPlayList) {
                handlePlayControl()
            } else {
                MyToas("正在准备播放中！请稍等~.~")
                handleNoPlayList()


            }

        }

        detail_sub_btn.setOnClickListener {
            if (mCurrentAlbum != null) {
                var issub = mSubscriptionPresenter.isSub(mCurrentAlbum!!)
                if (issub) {
                    mSubscriptionPresenter.deleteSubscription(mCurrentAlbum!!)
                } else {
                    mSubscriptionPresenter.addSubScription(mCurrentAlbum!!)
                }

            }
        }

    }

    private fun handleNoPlayList() {
        if (mCurrentTrack != null) {
            mPlayerPresenter.setPlayList(mCurrentTrack!!, DEFAULT_PLAY_INDEX)
        }
    }

    private fun handlePlayControl() {
        if (mPlayerPresenter.isPlaying()) {
            mPlayerPresenter.pause()
        } else {
            mPlayerPresenter.play()
        }
    }


    private fun createSuccessView(container: ViewGroup?): View? {
        var view = LayoutInflater.from(this).inflate(R.layout.item_detaill_list, container, false)

        mrefress_layout = view.findViewById(R.id.refress_layout)


        //线性布局管理器
        mdetailListAdapter = DetailListAdapter()
        mLlinearLayoutManager = LinearLayoutManager(this)
        mLlinearLayoutManager?.orientation = LinearLayoutManager.VERTICAL

        var recyclerView = view.findViewById<RecyclerView>(R.id.album_detail_list)

        recyclerView.layoutManager = mLlinearLayoutManager
        //适配器

        recyclerView.adapter = mdetailListAdapter

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.apply {
                    this.top = UIUtil.dip2px(view.context, 4.0)
                    this.bottom = UIUtil.dip2px(view.context, 4.0)
                    this.left = UIUtil.dip2px(view.context, 4.0)
                    this.right = UIUtil.dip2px(view.context, 4.0)
                }
            }
        })


        mdetailListAdapter?.setItemCilckListener(this)

        mrefress_layout?.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
                mAlbumDetailPresenter.loadMore()
                isLoadMore = true

            }

            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)

//                mrefress_layout?.onFinishRefresh()
            }
        })
        return view
    }


    override fun onDetailListLoaded(tacks: List<Track>) {

        if (isLoadMore && mrefress_layout != null) {
            mrefress_layout?.finishLoadmore()
            isLoadMore = false

        }

        this.mCurrentTrack = tacks
        if (tacks.isEmpty()) {
            if (muiLoader != null) {
                muiLoader?.updateStatus(UILoder.UlStatus.EMPTY)
            }
        }
        if (muiLoader != null) {
            muiLoader?.updateStatus(UILoder.UlStatus.SUCCESS)
        }
        mdetailListAdapter?.setData(tacks)

    }

    /**
     * 加载UI
     */
    override fun onAlbumLoaded(album: Album?) {
        this.mCurrentAlbum = album
        var id = album?.id?.toInt()
        mCurrentId = id!!
        mAlbumDetailPresenter.AlbumDetail(id, mCurrentPage)
        if (muiLoader != null) {
            muiLoader?.updateStatus(UILoder.UlStatus.LoADING)
        }
        tv_album_title.text = album?.albumTitle
        tv_album_author.text = album?.announcer?.nickname
        Picasso.with(this).load(album?.coverUrlLarge).into(iv_large_cover, object : Callback {
            override fun onSuccess() {
                ImageBlur().makeBlur(iv_large_cover, this@DetailActivity)
            }

            override fun onError() {

            }
        })

        Picasso.with(this).load(album?.coverUrlSmall).into(iv_small_cover)

    }

    /**
     * 错误回调
     */
    override fun onNetWorkError(errorcode: Int, errormsg: String?) {
        if (muiLoader != null) {
            muiLoader?.updateStatus(UILoder.UlStatus.NETWORK_ERROR)
        }
    }

    //点击重试
    override fun onRetryClick() {
        mAlbumDetailPresenter.AlbumDetail(mCurrentId, mCurrentPage)

    }

    override fun onItemClick(tacks: List<Track>, index: Int) {
        //设置数据

        PlayerPresenter.setPlayList(tacks, index)
        startActivity<PlayerActivity>()

    }

    /******************mPlayerPresenter *******************
     *
     */
    override fun onPlayStart() {
        updatdePlaySate(true)

    }

    override fun onPlayPause() {
        updatdePlaySate(false)

    }

    override fun onPlayStop() {
        updatdePlaySate(false)
    }

    private fun updatdePlaySate(playing: Boolean) {

        when (playing) {
            true -> {
                detail_play_control.setImageResource(R.drawable.selector_palyer_control_pause)
                if (mTrackTitle != null) {
                    paly_control_text.text = mTrackTitle
                    paly_control_text.isSelected = true

                }

            }
            false -> {
                detail_play_control.setImageResource(R.drawable.selector_palyer_control)
                paly_control_text.text = "点击播放"
            }
        }

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
        this.mTrackTitle = track.trackTitle
    }

    override fun updateListOrder(isReverse: Boolean) {
    }
//end

    override fun onLoadeMoreFinished(size: Int) {
        if (size > 0) {
            MyToas("下拉加载成功，加载到${size}条节目")
        } else {
            MyToas("没有更多节目")

        }
    }

    override fun onRefreshFinished(size: Int) {

    }

    fun MyToas(msg: String) {
        baseContext?.runOnUiThread { toast(msg) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAlbumDetailPresenter.unRegistViewCallBack(this)
        mPlayerPresenter.unRegistViewCallBack(this)
        mSubscriptionPresenter.unRegistViewCallBack(this)
    }


    //sub
    override fun onAddResult(isSuccess: Boolean) {

        if (isSuccess) {
            detail_sub_btn.text = "取消订阅"
        }

    }

    override fun onDeleteResult(isSuccess: Boolean) {
        if (isSuccess) {
            detail_sub_btn.text = "+ 订阅"
        }
    }

    override fun onSubScriptionLoaded(album: List<Album>) {

    }


}
