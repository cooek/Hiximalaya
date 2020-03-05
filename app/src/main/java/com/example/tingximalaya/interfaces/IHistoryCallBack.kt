package com.example.tingximalaya.interfaces

import com.ximalaya.ting.android.opensdk.model.track.Track


/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/3/4$ 18:20$
 */
interface IHistoryCallBack {




    fun onHistoriesLoaded(track: List<Track>)

}
