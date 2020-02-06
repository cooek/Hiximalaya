package com.example.tingximalaya.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/5$ 15:02$
 */
class RoundRectImageView(context: Context, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs) {

    private val rounRatio = 0.1f
    private val path: Path by lazy { Path() }


    override fun onDraw(canvas: Canvas?) {

        path.addRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            rounRatio * width,
            rounRatio * height,
            Path.Direction.CW
        )
        canvas?.apply {
            this.save()
            this.clipPath(path)
        }
        super.onDraw(canvas)
        canvas?.restore()

    }
}
