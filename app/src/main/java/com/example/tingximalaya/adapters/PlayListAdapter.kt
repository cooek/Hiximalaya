package com.example.tingximalaya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tingximalaya.R
import com.example.tingximalaya.views.SobPopWindow
import com.ximalaya.ting.android.opensdk.model.track.Track
import org.jetbrains.anko.textColor


/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/23$ 15:39$
 */
@Suppress("DEPRECATION")
class PlayListAdapter : RecyclerView.Adapter<PlayListAdapter.InnerHolder>() {

    private var mItemClick: SobPopWindow.PlayListItemClickListenter? = null
    private var mdata: ArrayList<Track> = arrayListOf()

    private var mPlayIndex = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InnerHolder {

        return InnerHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_play_list,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return mdata.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        var track = mdata[position]
        var mText = holder.itemView.findViewById<TextView>(R.id.track_title)
        mText.text = track.trackTitle
        var mImageView = holder.itemView.findViewById<ImageView>(R.id.play_icon_iv)

        mText.textColor = when (mPlayIndex == position) {
            true -> holder.itemView.context.resources.getColor(R.color.mainbackcolor)
            false -> R.color.play_list_text_color
        }

        holder.itemView.setOnClickListener {
            if (mItemClick != null) {
                mItemClick?.onItemClick(position)
            }
        }
        mImageView.visibility = when (mPlayIndex == position) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }


    class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {


    }

    fun setData(list: List<Track>) {
        mdata.clear()
        mdata.addAll(list)
        notifyDataSetChanged()
    }

    //是否当前
    fun setCurrentPlayPosition(playindex: Int) {
        mPlayIndex = playindex
        notifyDataSetChanged()
    }

    fun setonItemClickListenter(playListItemClickListenter: SobPopWindow.PlayListItemClickListenter) {
        this.mItemClick = playListItemClickListenter
    }
}
