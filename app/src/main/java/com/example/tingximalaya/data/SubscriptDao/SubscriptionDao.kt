package com.example.tingximalaya.data.SubscriptDao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.tingximalaya.base.BaseAplication
import com.example.tingximalaya.interfaces.ISubDao
import com.example.tingximalaya.interfaces.ISubDaoCallBack
import com.example.tingximalaya.utils.Constants
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.Announcer
import java.lang.Exception


/**
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/26$ 17:17$
 */
@SuppressLint("StaticFieldLeak")
object SubscriptionDao : ISubDao {


    private var mISbuCallBack: ISubDaoCallBack? = null


    private var mcontext: Context? = null

    private var dbhelper: XimalayaDbHelper? = null


    init {
        mcontext = BaseAplication().AppContext()!!
        if (mcontext != null) {
            dbhelper = XimalayaDbHelper(mcontext!!)
        }
    }


    override fun addAlbum(album: Album) {
        var db: SQLiteDatabase? = null
        try {
            db = dbhelper?.writableDatabase
            db?.beginTransaction()
            val contentValue by lazy { ContentValues() }
            //封装
            contentValue.put(Constants.SUB_COVER_URL, album.coverUrlLarge)
            contentValue.put(Constants.SUB_TITLE, album.albumTitle)
            contentValue.put(Constants.SUB_DESCRIPTION, album.albumIntro)
            contentValue.put(Constants.SUB_TRACKS_COUNT, album.includeTrackCount)
            contentValue.put(Constants.SUB_PLAY_COUNT, album.playCount)
            contentValue.put(Constants.SUB_AUTHOR_NAME, album.announcer.nickname)
            contentValue.put(Constants.SUB_ALBUM_ID, album.id)

            db?.insert(Constants.SUB_TB_NAME, null, contentValue)
            db?.setTransactionSuccessful()
            if (mISbuCallBack != null) {
                mISbuCallBack?.addResult(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (mISbuCallBack != null) {
                mISbuCallBack?.addResult(false)
            }
        } finally {
            db?.close()
            db?.endTransaction()
        }

    }

    override fun delAlbum(album: Album) {
        var db: SQLiteDatabase? = null
        try {
            db = dbhelper?.writableDatabase
            db?.beginTransaction()

            db?.delete(
                Constants.SUB_TB_NAME, Constants.SUB_ALBUM_ID + "?",
                (album.id.toString() as List<String>).toTypedArray()
            )


            db?.setTransactionSuccessful()

            if (mISbuCallBack != null) {
                mISbuCallBack?.addResult(true)
            }
        } catch (e: Exception) {
            if (mISbuCallBack != null) {
                mISbuCallBack?.addResult(false)
            }
            e.printStackTrace()
        } finally {
            db?.close()
            db?.endTransaction()
        }

    }

    override fun listAlbum() {
        var list: ArrayList<Album> = ArrayList()
        var db: SQLiteDatabase? = null
        try {
            db = dbhelper?.readableDatabase
            db?.beginTransaction()
            var query = db?.query(Constants.SUB_TB_NAME, null, null, null, null, null, null)

            while (query?.moveToNext()!!) {
                var album = Album()
                var url = query.getString(query.getColumnIndex(Constants.SUB_COVER_URL))
                album.coverUrlLarge = url
                var title = query.getString(query.getColumnIndex(Constants.SUB_TITLE))
                album.albumTitle = title
                var description = query.getString(query.getColumnIndex(Constants.SUB_DESCRIPTION))
                album.albumIntro = description
                var trackcount = query.getString(query.getColumnIndex(Constants.SUB_TRACKS_COUNT))
                album.includeTrackCount = trackcount.toLong()
                var playcount = query.getString(query.getColumnIndex(Constants.SUB_PLAY_COUNT))
                album.playCount = playcount.toLong()
                var nickname = query.getString(query.getColumnIndex(Constants.SUB_AUTHOR_NAME))
                var announcer = Announcer()
                announcer.nickname = nickname
                album.announcer = announcer
                var id = query.getString(query.getColumnIndex(Constants.SUB_ALBUM_ID))
                album.id = title.toLong()
                list.add(album)
            }
            query.close()
            db?.setTransactionSuccessful()
            if (mISbuCallBack != null) {
                mISbuCallBack?.onSubListLoaded(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (mISbuCallBack != null) {
                mISbuCallBack?.addResult(false)
            }
        } finally {
            db?.close()
            db?.endTransaction()
        }
    }

    override fun setCallBack(callback: ISubDaoCallBack) {
        this.mISbuCallBack = callback
    }


}
