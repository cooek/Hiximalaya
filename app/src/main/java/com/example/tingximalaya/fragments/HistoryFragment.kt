package com.example.tingximalaya.fragments

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tingximalaya.PlayerActivity
import com.example.tingximalaya.R
import com.example.tingximalaya.adapters.TrackListAdapter
import com.example.tingximalaya.base.BaseApplication
import com.example.tingximalaya.base.BaseFragment
import com.example.tingximalaya.interfaces.IHistoryCallBack
import com.example.tingximalaya.presenters.HistoryPresenter
import com.example.tingximalaya.presenters.PlayerPresenter
import com.example.tingximalaya.views.ConfirmBoxDialog
import com.example.tingximalaya.views.ConfirmDialog
import com.example.tingximalaya.views.UILoder
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.startActivity
import org.w3c.dom.Text

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@moxfail.com$
 * @Date: 2020/2/3$ 14:54$
 */
class HistoryFragment : BaseFragment(), IHistoryCallBack, TrackListAdapter.ItemCilckListener,
    TrackListAdapter.ItemLongClickListener, ConfirmBoxDialog.OnDialogActionClickListener {


    private var mCurrentTrack: Track? = null
    private var muiLoder: UILoder? = null

    private var mHistoryRecyclerView: RecyclerView? = null

    private val mTrackListAdapter = TrackListAdapter()

    private var mTwinklingRefreshLayout: TwinklingRefreshLayout? = null

    private val mPlayerPresenter by lazy { PlayerPresenter }

    private val mHistoryPresenter by lazy { HistoryPresenter }

    override fun onSubViewLoad(layoutInflater: LayoutInflater, container: ViewGroup?): View? {

        var rootView =
            layoutInflater.inflate(R.layout.fragment_history, container, false) as FrameLayout

        if (muiLoder == null) {
            muiLoder = object : UILoder(BaseApplication.getAppContext()) {
                override fun getSuccessView(container: ViewGroup?): View? {
                    return createSuccessView(container)
                }

                override fun getEmptView(): View? {
                    val itemview = LayoutInflater.from(context).inflate(R.layout.fragment_empty_view, this, false)

                    itemview.findViewById<TextView>(R.id.text_empyt).text = "历史记录为空噢！"

                    return itemview

                }

            }
        } else {
            if (muiLoder?.parent is ViewGroup) {
                (muiLoder?.parent as ViewGroup).removeView(muiLoder)
            }
        }
        mHistoryPresenter.RegisterViewcallback(this)
        runOnUiThread {
            muiLoder?.updateStatus(UILoder.UlStatus.LoADING)
        }
        rootView.addView(muiLoder)
        mHistoryPresenter.listHistories()
        return rootView

    }

    private fun createSuccessView(container: ViewGroup?): View? {
        var itemView = LayoutInflater.from(container?.context).inflate(R.layout.item_history, null)

        mTwinklingRefreshLayout = itemView.findViewById(R.id.over_history_view)
        mTwinklingRefreshLayout?.setEnableLoadmore(false)
        mTwinklingRefreshLayout?.setEnableRefresh(false)
        mTwinklingRefreshLayout?.setEnableOverScroll(true)
        mHistoryRecyclerView = itemView.findViewById(R.id.history_list)
        mHistoryRecyclerView?.layoutManager = LinearLayoutManager(container?.context)
        mTrackListAdapter.setItemCilckListener(this)
        mTrackListAdapter.setLongItemClickListener(this)
        mHistoryRecyclerView?.adapter = mTrackListAdapter
        mHistoryRecyclerView?.addItemDecoration(object : RecyclerView.ItemDecoration() {
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


        return itemView
    }

    override fun onHistoriesLoaded(track: List<Track>) {

        if (track.isEmpty()) {
            runOnUiThread {
                muiLoder?.updateStatus(UILoder.UlStatus.EMPTY)
            }
        } else {
            runOnUiThread {
                mTrackListAdapter.setData(track)
                muiLoder?.updateStatus(UILoder.UlStatus.SUCCESS)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHistoryPresenter.unRegistViewCallBack(this)
    }

    override fun onItemClick(tacks: List<Track>, index: Int) {
        mPlayerPresenter.IsRevers(true)
        mPlayerPresenter.setPlayList(tacks, index)
        startActivity<PlayerActivity>()

    }

    override fun onItemLongClick(tacks: Track) {
        this.mCurrentTrack = tacks
        var mConfirmBoxDialog = ConfirmBoxDialog(activity)
        mConfirmBoxDialog.SetOnDialogActionClickListener(this)
        mConfirmBoxDialog.show()

    }

    override fun onCancelClick() {


    }

    override fun onGiveUpClick(isCheck: Boolean) {

        if (mCurrentTrack != null) {
            if (!isCheck) {
                mHistoryPresenter.delHistory(mCurrentTrack!!)
            } else {
                mHistoryPresenter.cleanHistories()
            }
        }

    }


}
