package com.example.tingximalaya.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tingximalaya.R
import com.example.tingximalaya.adapters.PlayListAdapter
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import org.jetbrains.anko.imageResource

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/23$ 10:58$
 */

class SobPopWindow : PopupWindow {

    private var mMplayListPlayActionClickListenter: PlayListPlayActionClickListenter? = null
    private var mContext: Context? = null

    private var mview: View? = null

    private var mCloseBtn: TextView? = null

    private var mRecyclerView: RecyclerView? = null

    private var mTextView: TextView? = null

    private var mTextViewRigth: TextView? = null

    private var mImageView: ImageView? = null

    private var mImageViewRigth: ImageView? = null

    private var mLinearLayout: LinearLayout? = null

    private var mLinearLayoutrigth: LinearLayout? = null


    private val mPlayListAdapter by lazy { PlayListAdapter() }

    constructor(context: Context) : super(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ) {
        mContext = context
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        animationStyle = R.style.pop_antmation
        isOutsideTouchable = true
        mview = LayoutInflater.from(context).inflate(R.layout.pop_play_list, null, false)

        mRecyclerView = mview?.findViewById(R.id.play_list_rv)

        mTextView = mview?.findViewById(R.id.play_list_mode_tv)

        mImageView = mview?.findViewById(R.id.player_mode_switch_iv)

        mLinearLayout = mview?.findViewById(R.id.play_list_mode_line)

        mLinearLayoutrigth = mview?.findViewById(R.id.paly_list_order_container)

        mImageViewRigth = mview?.findViewById(R.id.play_list_order_iv)

        mTextViewRigth = mview?.findViewById(R.id.play_list_order_tv)


        initView()
        contentView = mview
    }

    private fun initView() {
        mCloseBtn = mview?.findViewById<TextView>(R.id.play_list_close).also {
            it?.setOnClickListener {
                this.dismiss()
            }
        }

        mLinearLayout?.setOnClickListener {
            if (mMplayListPlayActionClickListenter != null) {
                mMplayListPlayActionClickListenter?.onPlayModeClick()
            }
        }
        mRecyclerView.apply {
            this?.layoutManager = LinearLayoutManager(mContext)
            this?.adapter = mPlayListAdapter
        }

        mLinearLayoutrigth?.setOnClickListener {
            if (mMplayListPlayActionClickListenter != null) {
                mMplayListPlayActionClickListenter?.onOrderClick()
            }
        }


    }


    fun updateOrderIcon(isOrder: Boolean) {
        when (isOrder) {
            true -> {
                mImageViewRigth?.imageResource = R.drawable.selector_palyer_mode_list_order
                mTextViewRigth?.text = "顺序播放"
            }
            false -> {
                mImageViewRigth?.imageResource = R.drawable.selector_palyer_mode_list_rever
                mTextViewRigth?.text = "逆序播放"

            }
        }
    }

    fun setListDate(list: List<Track>) {
        mPlayListAdapter.setData(list)
    }

    fun setCureentPlayPosition(playindex: Int) {
        mPlayListAdapter.setCurrentPlayPosition(playindex)
        mRecyclerView?.scrollToPosition(playindex)
    }


    fun setPlayListItemClickListenter(playListItemClickListenter: PlayListItemClickListenter) {
        mPlayListAdapter.setonItemClickListenter(playListItemClickListenter)
    }

    interface PlayListItemClickListenter {

        fun onItemClick(position: Int)
    }


    fun updataMode(mcurrentMode: XmPlayListControl.PlayMode) {
        updatePlayMode(mcurrentMode)
    }

    private fun updatePlayMode(mcurrentMode: XmPlayListControl.PlayMode) {
        var resId = R.drawable.selector_palyer_mode_list_order
        when (mcurrentMode) {
            XmPlayListControl.PlayMode.PLAY_MODEL_LIST -> {
                resId = R.drawable.selector_palyer_mode_list_order
//                MyToas("默认模式")

                mTextView?.text = "默认模式"

            }
            XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM -> {
                resId = R.drawable.selector_palyer_mode_random
//                MyToas("随机模式")
                mTextView?.text = "随机模式"

            }
            XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP -> {
                resId = R.drawable.selector_palyer_mode_list_looper
//                MyToas("循环模式")
                mTextView?.text = "循环模式"

            }
            XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP -> {
                resId = R.drawable.selector_palyer_mode_single_loop
//                MyToas("单曲模式")
                mTextView?.text = "单曲模式"

            }

        }
        mImageView?.setImageResource(resId)

    }


    fun setPlayListPlayModeClickListenter(playListPlayActionClickListenter: PlayListPlayActionClickListenter) {
        this.mMplayListPlayActionClickListenter = playListPlayActionClickListenter
    }

    interface PlayListPlayActionClickListenter {
        fun onPlayModeClick()

        fun onOrderClick()
    }
}
