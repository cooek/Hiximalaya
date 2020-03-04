package com.example.tingximalaya

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.DetailActivity
import com.example.tingximalaya.adapters.AlbumListAdapter
import com.example.tingximalaya.adapters.SearchRecommendAdapter
import com.example.tingximalaya.base.BaseActivity
import com.example.tingximalaya.interfaces.ISearchCallBack
import com.example.tingximalaya.presenters.AlbumDetailPresenter
import com.example.tingximalaya.presenters.SearchPresenter
import com.example.tingximalaya.utils.Constants
import com.example.tingximalaya.views.FlowTextLayouts
import com.example.tingximalaya.views.UILoder
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.word.HotWord
import com.ximalaya.ting.android.opensdk.model.word.QueryResult
import kotlinx.android.synthetic.main.activity_search.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.toast
import kotlin.collections.ArrayList

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/25$ 14:20$
 */


@Suppress("DEPRECATION")
class SearchActivity : BaseActivity(), ISearchCallBack {


    private val mSearchPlayerPresenter by lazy { SearchPresenter }


    private val mSearchRecommendAdapter by lazy { SearchRecommendAdapter() }

    private var mRefresh: TwinklingRefreshLayout? = null

    private var mUlLoader: UILoder? = null

    private var itemguorp: FlowTextLayouts? = null

    private var itemview: RecyclerView? = null

    private var itemRecommendView: RecyclerView? = null

    private var ipt: InputMethodManager? = null

    private var msuggest = true


    private val TIME_SHOW_IMM: Long = 500


    private val mAlbumListAdapte by lazy { AlbumListAdapter() }

    private val mResultContainer by lazy { FlowTextLayouts(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mSearchPlayerPresenter.RegisterViewcallback(this)
        mSearchPlayerPresenter.HotWord()

        iniEvent()
    }

    private fun iniEvent() {

        //退出
        search_backcall.setOnClickListener {
            finish()
        }

        //搜索
        search_btn.setOnClickListener {
            if (search_input.text.isEmpty()) {
                MyToas("搜索内容不能为空噢！")
                return@setOnClickListener
            }
            var keyword = search_input.text.toString()
            mSearchPlayerPresenter.doSearch(keyword)
            mUlLoader?.updateStatus(UILoder.UlStatus.LoADING)
        }


        //文字的改变
        search_input.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TextUtils.isEmpty(p0)) {
                    mSearchPlayerPresenter.HotWord()
                    search_input_delete.visibility = View.GONE
                } else {
                    search_input_delete.visibility = View.VISIBLE
                    if (msuggest) {
                        Words(p0.toString())
                    } else {
                        msuggest = true
                    }
                }
            }
        })



        search_input.postDelayed({
            search_input.requestFocus()
            ipt?.showSoftInput(search_input, InputMethodManager.SHOW_IMPLICIT)
        }, TIME_SHOW_IMM)


        if (mUlLoader == null) {
            mUlLoader = object : UILoder(this) {
                override fun getSuccessView(container: ViewGroup?): View? {
                    return SuccessView()
                }


            }
            search_container.addView(mUlLoader)

        }

        mUlLoader?.onRetryClikListeners(object : UILoder.onRetryClikListener {
            override fun onRetryClick() {
                mSearchPlayerPresenter.reSearch()
                mUlLoader?.updateStatus(UILoder.UlStatus.LoADING)
            }
        })

        search_input_delete.setOnClickListener {
            search_input.setText("")
        }

        mSearchRecommendAdapter.ItemClickListenter(object :
            SearchRecommendAdapter.onReccommendItemClickListener {
            override fun onItemClick(keyword: String) {
                //推荐热词的点击
                msuggest = false

                switchSearch(keyword)
            }
        })

        //刷新
        mRefresh?.setOnRefreshListener(object : RefreshListenerAdapter() {

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                mSearchPlayerPresenter.loadMore()


            }
        })


        mAlbumListAdapte.ItemClickListenter(object :
            AlbumListAdapter.onReccommendItemClickListener {
            override fun onItemClick(postion: Int, album: Album) {
                AlbumDetailPresenter.TarGetAlbum(album)
                startActivity<DetailActivity>()
            }

        })

    }

    private fun Words(p0: String) {
        mSearchPlayerPresenter.RecommendWord(p0)
    }


    private fun SuccessView(): View? {

        var view = LayoutInflater.from(this).inflate(R.layout.search_result, null)

        //刷新控件
        mRefresh = view.findViewById(R.id.search_refresh_layout)

        mRefresh?.setEnableRefresh(false)


        itemview = view.findViewById(R.id.result_list_view)

        itemguorp = view.findViewById(R.id.flow_text_layout)

        itemguorp?.setClickListener { text ->
            msuggest = false
            switchSearch(text)
        }
        itemview?.layoutManager = LinearLayoutManager(this)
//
        itemview?.adapter = mAlbumListAdapte

        itemview?.addItemDecoration(object : RecyclerView.ItemDecoration() {
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

        itemRecommendView = view.findViewById(R.id.serch_recommend_view)

        itemRecommendView?.layoutManager = LinearLayoutManager(this)

        itemRecommendView?.addItemDecoration(object : RecyclerView.ItemDecoration() {
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
        itemRecommendView?.adapter = mSearchRecommendAdapter

        return view
    }

    private fun switchSearch(text: String) {
        if (text.isEmpty()) {
            MyToas("搜索内容不能为空噢！")
            return
        }

        search_input.setText(text)
        search_input.setSelection(text.length)
        mSearchPlayerPresenter.doSearch(text)
        mUlLoader?.updateStatus(UILoder.UlStatus.LoADING)
    }

    override fun onSearchResultLoaded(result: List<Album>) {
        hanleSearch(result)

        ipt?.hideSoftInputFromWindow(
            search_input.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun hanleSearch(result: List<Album>) {
        hideSucccessView()
        if (itemguorp != null) {
            mRefresh?.visibility = View.VISIBLE
        }
        ipt = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        mAlbumListAdapte.setData(result)
        mUlLoader?.updateStatus(UILoder.UlStatus.SUCCESS)
    }

    override fun onHotWordLoaded(hotWordList: List<HotWord>) {
        if (hotWordList.isEmpty()) {
            mUlLoader?.updateStatus(UILoder.UlStatus.EMPTY)
            return
        }
        hideSucccessView()
        itemguorp?.visibility = View.VISIBLE


        mUlLoader?.updateStatus(UILoder.UlStatus.SUCCESS)
        var hotWords: ArrayList<String> = ArrayList()
        hotWords.clear()
        for (hotWord in hotWordList) {
            var hearchword = hotWord.searchword
            hotWords.add(hearchword)
        }
        hotWords.sort()
        itemguorp?.setTextContents(hotWords)

    }

    override fun inLoadMoreResult(result: List<Album>, isOkay: Boolean) {
        mRefresh?.finishLoadmore()
        if (isOkay) {
            hanleSearch(result)
        } else {
            MyToas("没有更多内容啦！")
        }
    }

    //热词搜索回调
    override fun onRecommendWordLoaded(keyWorkList: List<QueryResult>) {
        mSearchRecommendAdapter.setData(keyWorkList)
        mUlLoader?.updateStatus(UILoder.UlStatus.SUCCESS)
        hideSucccessView()
        itemRecommendView?.visibility = View.VISIBLE
    }

    private fun hideSucccessView() {
        itemRecommendView?.visibility = View.GONE
        mRefresh?.visibility = View.GONE
        itemguorp?.visibility = View.GONE
    }

    override fun onError(p0: Int, p1: String?) {
        mUlLoader?.updateStatus(UILoder.UlStatus.NETWORK_ERROR)
    }


    override fun onDestroy() {
        super.onDestroy()
        mSearchPlayerPresenter.unRegistViewCallBack(this)
    }

    fun MyToas(msg: String) {
        baseContext?.runOnUiThread { toast(msg) }
    }

}
