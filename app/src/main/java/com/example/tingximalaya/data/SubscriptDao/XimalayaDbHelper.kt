package com.example.tingximalaya.data.SubscriptDao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tingximalaya.utils.Constants

/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/26$ 17:01$
 */
class XimalayaDbHelper : SQLiteOpenHelper {


    constructor(context: Context) : super(
        context,
        Constants.DB_NAME,
        null,
        Constants.DB_VERSION_CODE
    )

    override fun onCreate(p0: SQLiteDatabase?) {

        //创建数据表
        //订阅相关的字段
        //图片、title、描述、播放量、节目数量、作者名称（详情界面）专辑id
        val subTbSql = "create table " + Constants.SUB_TB_NAME + "(" +
                Constants.SUB_ID + " integer primary key autoincrement, " +
                Constants.SUB_COVER_URL + " varchar, " +
                Constants.SUB_TITLE + " varchar," +
                Constants.SUB_DESCRIPTION + " varchar," +
                Constants.SUB_PLAY_COUNT + " integer," +
                Constants.SUB_TRACKS_COUNT + " integer," +
                Constants.SUB_AUTHOR_NAME + " varchar," +
                Constants.SUB_ALBUM_ID + " integer" +
                ")"
        p0?.execSQL(subTbSql)
        //创建历史记录表
//        val historyTbSql = "create table " + Constants.HISTORY_TB_NAME + "(" +
//                Constants.HISTORY_ID + " integer primary key autoincrement, " +
//                Constants.HISTORY_TRACK_ID + " integer, " +
//                Constants.HISTORY_TITLE + " varchar," +
//                Constants.HISTORY_COVER + " varchar," +
//                Constants.HISTORY_PLAY_COUNT + " integer," +
//                Constants.HISTORY_DURATION + " integer," +
//                Constants.HISTORY_AUTHOR + " varchar," +
//                Constants.HISTORY_UPDATE_TIME + " integer" +
//                ")"
//        p0?.execSQL(historyTbSql)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
