package com.example.tingximalaya.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.example.tingximalaya.R
import kotlinx.coroutines.Runnable

/**
 * @return:$
 * @since: 1.0.0
 * @Author:coek@foxmail.com$
 * @Date: 2020/2/14$ 14:59$
 */
class LoadingView : ImageView {

    private var rotatDegree: Float = 0f

    private var nNeedRotate = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setImageResource(R.mipmap.loading)
    }

    /**
     * 绑定到窗口的时候
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        nNeedRotate = true
        post(object : Runnable {
            override fun run() {
                rotatDegree += 30
                rotatDegree = if (rotatDegree <= 360) {
                    rotatDegree
                } else 0f
                invalidate()
                if (nNeedRotate) {
                    postDelayed(this, 200)
                }
            }

        })
    }

    /**
     * 解绑
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        nNeedRotate = false
    }

    override fun onDraw(canvas: Canvas?) {
        /**
         *  角度
         *  x轴
         *  y轴
         */
        canvas?.rotate(rotatDegree, width / 2f, height / 2f)
        super.onDraw(canvas)

    }
}
