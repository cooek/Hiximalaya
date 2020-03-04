package com.example.tingximalaya.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tingximalaya.R
import com.ximalaya.ting.android.opensdk.model.word.QueryResult


/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/26$ 10:39$
 */
class SearchRecommendAdapter : RecyclerView.Adapter<SearchRecommendAdapter.InnerHolder>() {

    private var mdata: ArrayList<QueryResult> = ArrayList()

    private var itemClickListener: onReccommendItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {

        var itemview = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_recommend, parent, false)
        return InnerHolder(itemview)
    }

    override fun getItemCount(): Int {

        return mdata.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {

        var textview = holder.itemView.findViewById<TextView>(R.id.recommend_title)
        var queryResult = mdata[position]

        textview.text = queryResult.keyword


        holder.itemView.setOnClickListener {
            if (itemClickListener != null) {
                itemClickListener?.onItemClick(queryResult.keyword)
            }
        }
    }

    fun setData(keyWorkList: List<QueryResult>) {
        mdata.clear()
        mdata.addAll(keyWorkList)
        notifyDataSetChanged()
    }


    class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {


    }


    fun ItemClickListenter(itemClickListener: onReccommendItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    interface onReccommendItemClickListener {
        fun onItemClick(keyword: String)
    }
}
