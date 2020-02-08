package com.example.tingximalaya.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tingximalaya.R
import com.example.tingximalaya.adapters.RecommendListAdapter
import com.example.tingximalaya.base.BaseFragment
import com.example.tingximalaya.interfaces.IRecommendViewCallBack
import com.example.tingximalaya.presenters.RecommendPresenter
import com.example.tingximalaya.utils.Constants
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList
import kotlinx.android.synthetic.main.fragment_recommend.*
import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@moxfail.com$$
 * @Date: 2020/2/3$ 14:53$
 */
class RecommendFragment : BaseFragment(), IRecommendViewCallBack {


    private val recommendPresenter: RecommendPresenter by lazy { RecommendPresenter }

    private val recommendListAdapter: RecommendListAdapter by lazy { RecommendListAdapter() }

    /**
     * IllegalArgumentException
     */
    private var linearLayoutManager: LinearLayoutManager? = null

    private var rootView: View? = null

    /**
     * onCreateView 方法
     */
    override fun onSubViewLoad(layoutInflater: LayoutInflater, container: ViewGroup?): View? {
        rootView = layoutInflater.inflate(R.layout.fragment_recommend, container, false)
        return rootView
    }

    /**
     * onActivityCreated
     * 初始化控件相关
     */

    override fun initView() {
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager?.orientation = LinearLayoutManager.VERTICAL
        recommend_list.layoutManager = linearLayoutManager

        recommend_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
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
        recommendPresenter.RegisterViewcallback(this).apply {
            recommendPresenter.getRecommendList()
        }
        recommend_list.adapter = recommendListAdapter
    }


    override fun onRecommendListLoaded(reuslt: List<Album>) {
        //注意 配置了androidx的同学 RecyclerView 在androidx.recyclerview.widget.RecyclerView 这里
        recommendListAdapter?.setData(reuslt)
    }

    override fun onLoaderMore(result: List<Album>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRefreshMore(result: List<Album>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 界面销毁时 销毁引用
     */
    override fun onDestroyView() {
        super.onDestroyView()
        if (recommendPresenter != null) {
            recommendPresenter.unRegisterViewcallback(this)
        }
    }
}
