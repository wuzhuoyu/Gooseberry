package com.yuu.android.component.gooseberry.bridge.hb

import android.net.Uri
import com.yuu.android.component.gooseberry.config.HybridBridgeConfig
import com.yuu.android.component.gooseberry.webview.hybrid_webview.HybridWebView


/**
 * @InterfaceName : HybridBridgeApi
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/17 14:47
 */

interface HybridBridgeApi {

    fun init(bridgeConfig: HybridBridgeConfig):Boolean

    fun handlerNativeApi(uri: Uri):Boolean

    fun <T> handlerJavascriptApi(hybridWebView: HybridWebView, response: HybridBridgeResponse<T>,isOpenEncryption:Boolean)
}