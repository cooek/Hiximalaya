package com.example.tingximalaya

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.tingximalaya.adapters.IndiocatorAdapter
import com.example.tingximalaya.adapters.MainContentAdapter
import com.example.tingximalaya.utils.Logutils
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


class MainActivity : FragmentActivity() ,AnkoLogger{

    private var indicator: IndiocatorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initEvent()

    }

    private fun initEvent() {
        indicator?.setOnIndicatorTapClickListener(object :
            IndiocatorAdapter.OnIndicatorTapClickListener {
            override fun onTabClick(index: Int) {
                if (content_pager != null) {
                    content_pager.setCurrentItem(index)
                }
            }

        })
    }

    private fun initView() {
        //指示器相关
        main_indicator.setBackgroundColor(
            ContextCompat.getColor(this@MainActivity, R.color.mainbackcolor)
        )
        //初始化-传递参数-全局变量
        indicator = IndiocatorAdapter(this)
        val commonNavigator = CommonNavigator(this)
        //指示器适配器
        commonNavigator.adapter = indicator
        //文本自我调节宽高
        commonNavigator.isAdjustMode = true
        main_indicator.navigator = commonNavigator
        //ViewPager适配器
        content_pager.adapter = MainContentAdapter(supportFragmentManager)
        //绑定viewpager and indicator
        ViewPagerHelper.bind(main_indicator, content_pager)


    }


}
