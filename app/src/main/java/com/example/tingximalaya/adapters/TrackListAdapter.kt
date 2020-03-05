package com.example.tingximalaya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tingximalaya.R
import com.ximalaya.ting.android.opensdk.model.track.Track
import java.text.SimpleDateFormat


/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/21$ 14:21$
 */
class TrackListAdapter : RecyclerView.Adapter<TrackListAdapter.Innerholder>() {

    private  var mItemLongClickListener: ItemLongClickListener?=null
    private var mDetailList: ArrayList<Track> = ArrayList()


    private var mitemListener: ItemCilckListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Innerholder {
        return Innerholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_album_detail,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {

        return mDetailList.size
    }

    override fun onBindViewHolder(holder: Innerholder, position: Int) {
        holder.itemView.tag = position

        var itemview =  holder.itemView

        itemview.setOnClickListener {
            if (mitemListener != null) {
                mitemListener?.onItemClick(mDetailList,position)
            }
        }
        itemview.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                if (mItemLongClickListener != null) {
                    mItemLongClickListener?.onItemLongClick(mDetailList[position])
                }
                return true
            }
        })
        holder.setData(mDetailList[position], position)

    }


    fun setData(tacks: List<Track>) {
        mDetailList.clear()
        mDetailList.addAll(tacks)
        notifyDataSetChanged()


    }

    class Innerholder(view: View) : RecyclerView.ViewHolder(view) {
        private var mSimpledateformat = SimpleDateFormat("yyyy-MM-dd")
        private var mDurationformat = SimpleDateFormat("mm-ss")
        var ordertitle = view.findViewById<TextView>(R.id.order_text)
        var detail_title = view.findViewById<TextView>(R.id.detail_item_title)
        var detail_playcount = view.findViewById<TextView>(R.id.detail_item_play_count)
        var detail_duration = view.findViewById<TextView>(R.id.detail_item_duration)
        var detail_time = view.findViewById<TextView>(R.id.detail_item_update_time)


        fun setData(
            track: Track,
            position: Int
        ) {
            ordertitle.text = (position+1).toString()
            detail_title.text = track.trackTitle
            detail_playcount.text = track.playCount.toString()

            var update = mSimpledateformat.format(track.updatedAt)
            detail_time.text = update
            var durction = track.duration * 1000
            var mmiss = mDurationformat.format(durction)
            detail_duration.text = mmiss

        }


    }

    fun setItemCilckListener(itemCilckListener: ItemCilckListener) {
        this.mitemListener = itemCilckListener
    }

    interface ItemCilckListener {
        fun onItemClick(tacks: List<Track>,index:Int)
    }


    fun setLongItemClickListener(itemLongClickListener:ItemLongClickListener){
        this.mItemLongClickListener =itemLongClickListener
    }

    interface  ItemLongClickListener{
        fun onItemLongClick(tacks: Track)
    }


}

