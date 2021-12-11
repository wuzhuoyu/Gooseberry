package com.yuuoffice.hybrid.component.bridge.hb

import android.net.Uri
import com.yuuoffice.hybrid.component.config.HybridBridgeConfig
import com.yuuoffice.hybrid.component.webview.hybrid_webview.HybridWebView


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