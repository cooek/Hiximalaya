package com.example.tingximalaya.adapters

import android.content.Context
import android.graphics.Color
import com.example.tingximalaya.R
import com.example.tingximalaya.utils.Logutils
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmailcom$
 * @Date: 2020/1/30$ 21:37$
 */
class IndiocatorAdapter(context: Context?) : CommonNavigatorAdapter() {

    private lateinit var onIndicatorTapClickListeners: OnIndicatorTapClickListener

    private var contexts: Context? = null

    private var list: Array<String>? = null

    init {
        this.contexts = context
        this.list = contexts?.resources?.getStringArray(R.array.indicator_name)
    }


    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
        val simplePagerTitleView = ColorTransitionPagerTitleView(context)
        simplePagerTitleView.normalColor = Color.GRAY
        simplePagerTitleView.selectedColor = Color.WHITE
        simplePagerTitleView.text = list?.get(index)
        simplePagerTitleView.setOnClickListener {
            if(onIndicatorTapClickListeners!=null){
                onIndicatorTapClickListeners.onTabClick(index)

            }

        }
        return simplePagerTitleView
    }


    override fun getCount(): Int {
        if (list != null) {
            return list!!.size
        }
        return 0
    }

    override fun getIndicator(context: Context?): IPagerIndicator {
        val linePagerIndicator = LinePagerIndicator(context)
        linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
        linePagerIndicator.setColors(Color.WHITE)
        return linePagerIndicator
    }

    public  fun setOnIndicatorTapClickListener(onIndicatorTapClickListener:OnIndicatorTapClickListener){
        this.onIndicatorTapClickListeners =onIndicatorTapClickListener
    }

    public interface OnIndicatorTapClickListener{
        fun onTabClick(index:Int)
    }



}
