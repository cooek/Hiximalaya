package com.example.tingximalaya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tingximalaya.R
import com.squareup.picasso.Picasso


import com.ximalaya.ting.android.opensdk.model.album.Album


/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/4$ 16:45$
 */
class RecommendListAdapter : RecyclerView.Adapter<RecommendListAdapter.InnerHolder>() {


    private var monReccommendItemClickListener: onReccommendItemClickListener? = null

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemView.tag = position
        holder.itemView.setOnClickListener {
            if (monReccommendItemClickListener != null) {
                monReccommendItemClickListener?.onItemClick(it.tag as Int,mdata[position])
            }
        }
        holder.setdata(mdata[position])


    }


    private var mdata: ArrayList<Album> = arrayListOf()

    class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {

        var album_cover: ImageView = itemView!!.findViewById(R.id.album_cover)
        var album_title_tv: TextView = itemView!!.findViewById(R.id.album_title_tv)

        var album_descripton: TextView = itemView!!.findViewById(R.id.album_descripton_tv)
        var album_play_count: TextView = itemView!!.findViewById(R.id.album_play_count)
        var album_content_size: TextView = itemView!!.findViewById(R.id.album_content_size)

        fun setdata(album: Album) {
            album_title_tv.text = album.albumTitle
            album_descripton.text = album.albumIntro
            album_play_count.text = album.playCount.toString()
            album_content_size.text = album.includeTrackCount.toString()
            Picasso.with(itemView.context).load(album.coverUrlLarge).into(album_cover)
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendListAdapter.InnerHolder {
        return InnerHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recommend,
                parent,
                false
            )
        )

//        return  InnerHolder(HomeItemView(parent?.context))
    }

    override fun getItemCount(): Int {
        if (mdata != null) {
            return mdata.size
        }
        return 0
    }


    fun setData(albumList: List<Album>) {
        if (albumList != null) {
            mdata.clear()
            mdata.addAll(albumList)
        }
        //更新UI
        notifyDataSetChanged()
    }


    fun ItemClickListenter(itemClickListener: onReccommendItemClickListener) {
        this.monReccommendItemClickListener = itemClickListener
    }


    interface onReccommendItemClickListener {
        fun onItemClick(postion: Int,album: Album)
    }
}
