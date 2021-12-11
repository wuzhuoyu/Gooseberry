package com.yuuoffice.hybrid.component.api

import com.yuuoffice.hybrid.component.webview.hybrid_webview.listener.HybridListener


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