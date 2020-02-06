package com.example.tingximalaya.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.tingximalaya.R

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/6$ 15:36$
 */
class HomeItemView : RelativeLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_recommend, this)
    }

}
