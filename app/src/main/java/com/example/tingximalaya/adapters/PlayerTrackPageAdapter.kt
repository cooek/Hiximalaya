package com.example.tingximalaya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.example.tingximalaya.R
import com.squareup.picasso.Picasso
import com.ximalaya.ting.android.opensdk.model.track.Track

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/22$ 14:45$
 */
class PlayerTrackPageAdapter : PagerAdapter() {

    private var mdata: ArrayList<Track> = arrayListOf()


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {

        return mdata.size
    }


    fun setData(list: List<Track>) {
        mdata.clear()
        mdata.addAll(list)
        notifyDataSetChanged()
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        var view = LayoutInflater.from(container.context)
            .inflate(R.layout.item_track_page, container, false)
        container.addView(view)
        var imageview = view.findViewById<ImageView>(R.id.track_Pager_item)
        var imgurl = mdata[position].coverUrlLarge
        if (imgurl != null) {
//            Picasso.with(container.context).load(imgurl).into(imageview)
            Glide.with(container.context)
                .load(imgurl).placeholder(R.mipmap.timg)
                .into(imageview)
        }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        var view = obj as View
        container.removeView(view)

    }
}
