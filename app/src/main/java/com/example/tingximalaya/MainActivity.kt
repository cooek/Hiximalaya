package com.example.tingximalaya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tingximalaya.utils.Logutils
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.category.Category
import com.ximalaya.ting.android.opensdk.model.category.CategoryList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var map:HashMap<String?,String?> = HashMap()

        CommonRequest.getCategories(map,object :IDataCallBack<CategoryList>{
            override fun onSuccess(p0: CategoryList?) {
                //知道肯定不为空返回一个可变的list
                var category:MutableList<Category> = p0!!.categories
                val size:Int = category.size
                for ( (i,category) in category.withIndex()){
                    Logutils.d(MainActivity::class.java.name," ${i} item "+category.categoryName)
                }
                Logutils.d(MainActivity::class.java.name, "category$size")

            }

            override fun onError(p0: Int, p1: String?) {
                println("error code -> $p0 error msg $p1")
            }

        })

    }


}
