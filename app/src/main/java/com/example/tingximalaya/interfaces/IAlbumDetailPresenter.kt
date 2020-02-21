package com.example.tingximalaya.interfaces

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:cooek@foxmail.com$
 * @Date: 2020/2/21$ 9:50$
 */
interface IAlbumDetailPresenter {

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
    fun AlbumDetail(albumId: Int, Page: Int)

    /**
     * 注册
     */
    fun registViewCallBack(ialbumDetailViewCallBack:IAlbumDetailViewCallBack)

    /**
     * 取消注册
     */
    fun unregistViewCallBack(ialbumDetailViewCallBack:IAlbumDetailViewCallBack)
}
