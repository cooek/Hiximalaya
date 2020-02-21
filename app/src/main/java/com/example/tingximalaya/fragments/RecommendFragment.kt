package com.example.tingximalaya.fragments

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.DetailActivity
import com.example.tingximalaya.R
import com.example.tingximalaya.adapters.RecommendListAdapter
import com.example.tingximalaya.base.BaseFragment
import com.example.tingximalaya.interfaces.IRecommendViewCallBack
import com.example.tingximalaya.presenters.AlbumDetailPresenter
import com.example.tingximalaya.presenters.RecommendPresenter
import com.example.tingximalaya.views.UILoder
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.android.synthetic.main.fragment_recommend.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.jetbrains.anko.support.v4.startActivity

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@moxfail.com$$
 * @Date: 2020/2/3$ 14:53$
 */
class RecommendFragment : BaseFragment(), IRecommendViewCallBack, UILoder.onRetryClikListener,
    RecommendListAdapter.onReccommendItemClickListener {


    private var muiLoder: UILoder? = null


    private val mRecommendPresenter: RecommendPresenter by lazy { RecommendPresenter }

    private val mRecommendListAdapter: RecommendListAdapter by lazy { RecommendListAdapter() }

    /**
     * IllegalArgumentException
     */
    private var mLlinearLayoutManager: LinearLayoutManager? = null

    private var rootView: View? = null

    /**
     * onCreateView 方法
     */
    override fun onSubViewLoad(layoutInflater: LayoutInflater, container: ViewGroup?): View? {

        muiLoder = object : UILoder(context) {

            override fun getSuccessView(container: ViewGroup?): View? {

                return cretaeSuccessView(layoutInflater, container)
            }

        }
        mRecommendPresenter.RegisterViewcallback(this)
        mRecommendPresenter.getRecommendList()

        if (muiLoder?.parent is ViewGroup) {
            var mload = muiLoder?.parent as ViewGroup
            mload.removeView(mload)

        }
        muiLoder?.onRetryClikListeners(this)
        return muiLoder

    }

    private fun cretaeSuccessView(layoutInflater: LayoutInflater, container: ViewGroup?): View? {

        rootView = layoutInflater.inflate(R.layout.fragment_recommend, container, false)

        return rootView
    }

    /**
     * onActivityCreated
     * 初始化控件相关
     */

    override fun initView() {
        mLlinearLayoutManager = LinearLayoutManager(context)
        mLlinearLayoutManager?.orientation = LinearLayoutManager.VERTICAL
        recommend_list.layoutManager = mLlinearLayoutManager

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

        recommend_list.adapter = mRecommendListAdapter
        mRecommendListAdapter.ItemClickListenter(this)


    }


    override fun onRecommendListLoaded(reuslt: List<Album>) {
        //注意 配置了androidx的同学 RecyclerView 在androidx.recyclerview.widget.RecyclerView 这里
        mRecommendListAdapter.setData(reuslt)
        muiLoder?.updateStatus(UILoder.UlStatus.SUCCESS)
    }

    /**
     * 界面销毁时 销毁引用
     */
    override fun onDestroyView() {
        super.onDestroyView()

        mRecommendPresenter.unRegisterViewcallback(this)

    }

    override fun onNetworkError() {
        muiLoder?.updateStatus(UILoder.UlStatus.NETWORK_ERROR)
    }

    override fun onEmpty() {
        muiLoder?.updateStatus(UILoder.UlStatus.EMPTY)

    }

    override fun onLoading() {
        muiLoder?.updateStatus(UILoder.UlStatus.LoADING)

    }

    //网络错误接口回调
    override fun onRetryClick() {
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList()
        }
    }

    //点击事件
    override fun onItemClick(postion: Int, album: Album) {
        AlbumDetailPresenter.TarGetAlbum(album)
        startActivity<DetailActivity>()
    }


}
