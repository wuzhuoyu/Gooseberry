package com.yuu.android.component.gooseberry.api

import com.yuu.android.component.gooseberry.webview.hybrid_webview.listener.HybridListener


/**
 * @InterfaceName : HybridWebViewApi
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/12 18:24
 */

interface HybridWebViewApi {

    /**绑定事件监听*/
    fun injectHybridListener(listener:HybridListener)

}