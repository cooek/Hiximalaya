package com.example

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.example.tingximalaya.R
import com.example.tingximalaya.base.BaseActivity
import com.example.tingximalaya.interfaces.IAlbumDetailViewCallBack
import com.example.tingximalaya.presenters.AlbumDetailPresenter
import com.squareup.picasso.Picasso
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * @return:$
 * @since: 1.0.0
 * @Author:cooekU@foxmail.com$
 * @Date: 2020/2/21$ 9:14$
 */
class DetailActivity : BaseActivity(), IAlbumDetailViewCallBack {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.statusBarColor = Color.TRANSPARENT

        AlbumDetailPresenter.registViewCallBack(this)
    }



    override fun onDetailListLoaded(tacks: List<Track>) {


    }

    override fun onAlbumLoaded(album: Album?) {
        tv_album_title.text = album?.albumTitle
        tv_album_author.text = album?.announcer?.nickname
        Picasso.with(this).load(album?.coverUrlLarge).into(iv_large_cover)
    }


}
