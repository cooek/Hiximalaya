package com.example.tingximalaya.fragments

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.DetailActivity
import com.example.tingximalaya.R
import com.example.tingximalaya.adapters.AlbumListAdapter
import com.example.tingximalaya.base.BaseFragment
import com.example.tingximalaya.data.Constants
import com.example.tingximalaya.interfaces.ISubscriptCallBack
import com.example.tingximalaya.presenters.AlbumDetailPresenter
import com.example.tingximalaya.presenters.SubscriptionPresenter
import com.example.tingximalaya.views.ConfirmDialog
import com.example.tingximalaya.views.UILoder
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.ximalaya.ting.android.opensdk.model.album.Album
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Runnable
import net.lucode.hackware.magicindicator.buildins.UIUtil
import okhttp3.internal.platform.Android10Platform
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.startActivity

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@moxfail.com$
 * @Date: 2020/2/3$ 14:53$
 */
class SubscritFragment : BaseFragment(), ISubscriptCallBack,
    AlbumListAdapter.onReccommendItemClickListener, AlbumListAdapter.onAlbumItemLongClickListener,
    ConfirmDialog.OnDialogActionClickListener {


    private var mCurrentAlbum: Album? = null
    private var mSubscriptionPresenter: SubscriptionPresenter? = null

    private val mAlbumListAdapter = AlbumListAdapter()

    private var muiLoder: UILoder? = null

    private var mConfirmDialog: ConfirmDialog? = null

    private var mview: FrameLayout? = null

    private var mTwinklingRefreshLayout: TwinklingRefreshLayout? = null

    private var mSubRecyclerView: RecyclerView? = null

    override fun onSubViewLoad(layoutInflater: LayoutInflater, container: ViewGroup?): View? {
        mview = layoutInflater.inflate(R.layout.fragment_subscript, container, false) as FrameLayout
        mSubscriptionPresenter = SubscriptionPresenter
        mSubscriptionPresenter?.GetContext(container?.context!!)
        if (muiLoder == null) {
            muiLoder = object : UILoder(context) {
                override fun getSuccessView(container: ViewGroup?): View? {

                    return createsuccess(layoutInflater, container)
                }

                override fun getEmptView(): View? {
                    var itemview = LayoutInflater.from(context)
                        .inflate(R.layout.fragment_sub_script_view, this, false)
                    var itemtext: TextView = itemview.findViewById(R.id.text_sub_scription)
                    itemtext.text = "内容~为空赶紧去订阅吧！"
                    return itemview

                }
            }
        } else {
            if (muiLoder?.parent is ViewGroup) {
                (muiLoder?.parent as ViewGroup).removeView(muiLoder)
            }
        }
        mview?.addView(muiLoder)

        return mview
    }

    private fun createsuccess(layoutInflater: LayoutInflater, container: ViewGroup?): View? {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_sub_scription, null)
        mConfirmDialog = ConfirmDialog(activity)
        mSubscriptionPresenter?.RegisterViewcallback(this)
        mSubscriptionPresenter?.getSubScriptionList()
        mAlbumListAdapter.ItemClickListenter(this)
        mAlbumListAdapter.onSetonAlbumItemLongClickListener(this)


        mSubRecyclerView = itemView?.findViewById(R.id.sub_list)
        mTwinklingRefreshLayout = itemView?.findViewById(R.id.over_scroll_view)
        mTwinklingRefreshLayout?.setEnableLoadmore(false)
        mTwinklingRefreshLayout?.setEnableRefresh(false)
        mSubRecyclerView?.layoutManager = LinearLayoutManager(context)
        mSubRecyclerView?.adapter = mAlbumListAdapter
        mSubRecyclerView?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.apply {
                    this.top = UIUtil.dip2px(view.context, 5.0)
                    this.bottom = UIUtil.dip2px(view.context, 5.0)
                    this.left = UIUtil.dip2px(view.context, 5.0)
                    this.right = UIUtil.dip2px(view.context, 5.0)
                }
            }
        })

        runOnUiThread {
            muiLoder?.updateStatus(UILoder.UlStatus.LoADING)

        }
        return itemView

    }


    override fun onAddResult(isSuccess: Boolean) {


    }

    override fun onDeleteResult(isSuccess: Boolean) {

        MyToas(
            when (isSuccess) {
                true -> {
                    "取消订阅成功"
                }
                false -> {
                    "取消订阅失败"
                }
            }
        )
    }

    override fun onSubScriptionLoaded(album: List<Album>) {

        if (album.isEmpty()) {
            if (muiLoder != null) {
                runOnUiThread {
                    muiLoder?.updateStatus(UILoder.UlStatus.EMPTY)

                }
            }
        } else {
            runOnUiThread {
                muiLoder?.updateStatus(UILoder.UlStatus.SUCCESS)
            }
        }

        runOnUiThread {
            mAlbumListAdapter.setData(album)
        }


    }

    override fun onSubFull() {
        MyToas("订阅数量不能超过${Constants.MAX_HISTORY_COUNT}条啦~")

    }

    override fun onItemLongClick(album: Album) {
        this.mCurrentAlbum = album
        mConfirmDialog?.SetOnDialogActionClickListener(this)
        mConfirmDialog?.show()
    }

    //取消注册
    override fun onItemClick(postion: Int, album: Album) {
        AlbumDetailPresenter.TarGetAlbum(album)
        startActivity<DetailActivity>()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mSubscriptionPresenter?.unRegistViewCallBack(this)
    }

    //dialog
    override fun onCancelClick() {

        if (mCurrentAlbum != null) {
            mSubscriptionPresenter?.deleteSubscription(mCurrentAlbum!!)
        }
        mConfirmDialog?.dismiss()
    }

    override fun onGiveUpClick() {
        mConfirmDialog?.dismiss()

    }
    //end
}
