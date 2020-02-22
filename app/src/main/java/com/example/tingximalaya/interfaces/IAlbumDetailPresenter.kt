package com.example.tingximalaya.interfaces

import com.example.tingximalaya.base.IBasePresenter

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/21$ 9:50$
 */
interface IAlbumDetailPresenter:IBasePresenter<IAlbumDetailViewCallBack> {

    /**
     * 下拉刷新更多内容
     */
    fun pullRefreshMore()


    /**
     * 上拉加载更多
     */
    fun loadMore()


    /**
     * 获取专辑详情
     */
    fun AlbumDetail(albumId: Int, Page: Int?)

}
