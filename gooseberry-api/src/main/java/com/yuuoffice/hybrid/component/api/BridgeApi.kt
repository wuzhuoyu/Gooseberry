package com.yuuoffice.hybrid.component.api

import android.net.Uri
import com.yuuoffice.hybrid.component.bridge.Bridge
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridgeResponse
import com.yuuoffice.hybrid.component.config.HybridBridgeConfig
import com.yuuoffice.hybrid.component.webview.hybrid_webview.HybridWebView


/**
 * @InterfaceName : BridgeApi
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/27 17:59
 */

interface BridgeApi {

    fun setBridgeConfig(hybridBridgeConfig: HybridBridgeConfig):Bridge

    fun processNativeApi(uri: Uri):Boolean

    fun <T> processJavascriptApi(response : HybridBridgeResponse<T>)

    fun openLog(isOpenLog:Boolean):Bridge

    fun openEncryption(isOpenEncryption: Boolean): Bridge

    fun bindHybridWebView(hybridWebView: HybridWebView)

    fun unbindHybridWebView()

    fun destroy()

}