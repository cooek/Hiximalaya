package com.example.tingximalaya.interfaces

import com.example.tingximalaya.base.IBasePresenter
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/22$ 10:07$
 */
interface lPlayerPresenter : IBasePresenter<IPlayerCallBack> {


    /**
     * 播放
     */
    fun play()

    /**
     * 暂停
     */
    fun pause()

    /**
     *停止
     */
    fun stop()


    /**
     * 上一首
     */
    fun playPre()


    /**
     * 下一首
     */
    fun playNext()


    /**
     * 切换mode
     */
    fun switchPlayMode(mode: XmPlayListControl.PlayMode)


    /**
     * 获取播放列表
     */
    fun PlayList()

    /**
     * 根据节目列表进行播放
     */
    fun playByIndex(index: Int)


    /**
     * 切换进度
     */
    fun seekTo(progress: Int)


    /**
     *是否播放
     */

    fun isPlaying(): Boolean


    /**
     * 反转
     */
    fun reversePlayList()


    fun playByAlbum(id: Long)


}
