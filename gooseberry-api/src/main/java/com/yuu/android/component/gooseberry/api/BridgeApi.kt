package com.yuu.android.component.gooseberry.api

import android.net.Uri
import com.yuu.android.component.gooseberry.bridge.Bridge
import com.yuu.android.component.gooseberry.bridge.hb.HybridBridgeResponse
import com.yuu.android.component.gooseberry.config.HybridBridgeConfig
import com.yuu.android.component.gooseberry.webview.hybrid_webview.HybridWebView


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

    fun addUploadLogListener(upLoadLogListener:((String)->Unit)?) : Bridge

    fun openEncryption(isOpenEncryption: Boolean): Bridge

    fun bindHybridWebView(hybridWebView: HybridWebView)

    fun unbindHybridWebView()

    fun destroy()

}