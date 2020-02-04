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


class MainActivity : FragmentActivity() {

    private var indicator: IndiocatorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initEvent()

    }

    private fun initEvent() {
        indicator?.setOnIndicatorTapClickListener(object : IndiocatorAdapter.OnIndicatorTapClickListener {
            override fun onTabClick(index: Int) {
                Logutils.d(MainActivity::class.java.toString(), "点击了第$index"+"条目")
                if (content_pager != null) {
                    content_pager.setCurrentItem(index)
                }
            }

        })
    }

    private fun initView() {
        //指示器相关
        main_indicator.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.mainbackcolor))

        indicator = IndiocatorAdapter(this)

        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = indicator
        //获取support包
        val supportFragments = supportFragmentManager
        val mainContentAdapter = MainContentAdapter(supportFragments)
        //绑定viewpager  视图
        content_pager.adapter = mainContentAdapter
        main_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(main_indicator, content_pager)
    }


}
