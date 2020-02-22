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
import com.example.tingximalaya.presenters.AlbumDetailPresenter
import com.example.tingximalaya.presenters.PlayerPresenter
import com.example.tingximalaya.utils.ImageBlur
import com.example.tingximalaya.views.UILoder
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import kotlinx.android.synthetic.main.activity_detail.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.jetbrains.anko.startActivity

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooekU@foxmail.com$
 * @Date: 2020/2/21$ 9:14$
 */
class DetailActivity : BaseActivity(), IAlbumDetailViewCallBack, UILoder.onRetryClikListener,
    DetailListAdapter.ItemCilckListener {


    private var mCurrentPage = 1

    private var mCurrentId = -1

    private var mdetailListAdapter: DetailListAdapter? = null

    private var muiLoader: UILoder? = null

    private var mLlinearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.statusBarColor = Color.TRANSPARENT

        AlbumDetailPresenter.RegisterViewcallback(this)

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


    }

    private fun createSuccessView(container: ViewGroup?): View? {
        var view = LayoutInflater.from(this).inflate(R.layout.item_detaill_list, container, false)
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
        return view
    }


    override fun onDetailListLoaded(tacks: List<Track>) {

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

        var id = album?.id?.toInt()
        mCurrentId = id!!
        AlbumDetailPresenter.AlbumDetail(id!!, mCurrentPage)
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
        AlbumDetailPresenter.AlbumDetail(mCurrentId!!, mCurrentPage)

    }

    override fun onItemClick(tacks: List<Track>, index: Int) {
        //设置数据

        PlayerPresenter.setPlayList(tacks, index)
        startActivity<PlayerActivity>()

    }


}
