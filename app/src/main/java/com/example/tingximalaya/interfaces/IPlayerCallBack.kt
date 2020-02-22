package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/22$ 10:20$
 */
interface IPlayerCallBack {


    /**
     * 开始播放
     */
    fun onPlayStart()


    /**
     * 播放暂停
     */
    fun onPlayPause()


    /**
     * 停止
     */
    fun onPlayStop()


    /**
     * 播放错误
     */
    fun onPlayError()


    /**
     * 下一首播放
     */
    fun nextPlay(track: Track)


    /**
     * 上一首
     */

    fun onPrePlay(track: Track)


    /**
     * 播放列表数据
     */
    fun onListLoaded(list: List<Track>)


    /**
     * 播放模式
     */
    fun onPlayModeChange(mode: XmPlayListControl.PlayMode)


    /**
     * 进度条的改变
     */
    fun onProgressChange(currentProgress: Int, long: Int)


    /**
     * 广告加载
     */
    fun onAdLoading()

    /**
     * 加载完成
     */
    fun onAdFinished()


    /**
     * 标题
     */
    fun onTrackUpdate(track:Track,playindex :Int)
}

