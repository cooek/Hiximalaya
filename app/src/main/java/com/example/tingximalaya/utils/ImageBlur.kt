package com.example.tingximalaya.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView

import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.Element
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth




/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/21$ 11:12$
 */
class ImageBlur {
    fun makeBlur(imageview: ImageView, context: Context) {
        val drawable: BitmapDrawable = imageview.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
//        Bitmap blurred =
        val blurred = blurRenderSctipt(bitmap, 10, context) //second parametre is radius max:25
        imageview.setImageBitmap(blurred) //radius decide blur amount
    }

    private fun blurRenderSctipt(smallBitmap: Bitmap, radius: Int, context: Context):Bitmap {
        var smallBitmaps = RGB565toARGB888(smallBitmap)
        val bitmap =
            Bitmap.createBitmap(smallBitmaps.width, smallBitmaps.height, Bitmap.Config.ARGB_8888)
        val renderScript = RenderScript.create(context)
        val blurInput = Allocation.createFromBitmap(renderScript, smallBitmaps)
        val blurOutput = Allocation.createFromBitmap(renderScript, bitmap)
        val blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        blur.setInput(blurInput)
        blur.setRadius(radius.toFloat()) // radius must be 0 < r <= 25
        blur.forEach(blurOutput)
        blurOutput.copyTo(bitmap)
        renderScript.destroy()
        return bitmap


    }

    private fun RGB565toARGB888(img: Bitmap): Bitmap {
        val numPixels = img.width * img.height
        val pixels = IntArray(numPixels)

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.width, 0, 0, img.width, img.height)

        //Create a Bitmap of the appropriate format.
        val result = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888)

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
        return result
    }

}
