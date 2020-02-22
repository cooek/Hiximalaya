package com.example.tingximalaya.base

/**$
 * @return:$
 * @since: 1.0.0
 * @Author:$
 * @Date: 2020/2/22$ 10:08$
 */
interface IBasePresenter<T> {

    /**
     * 取消
     */
    fun unRegistViewCallBack(t:T)


    /**
     * 注册
     */
    fun RegisterViewcallback(t:T)

}
