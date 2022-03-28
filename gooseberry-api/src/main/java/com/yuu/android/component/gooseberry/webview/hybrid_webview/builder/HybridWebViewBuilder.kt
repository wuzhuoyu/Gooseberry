package com.yuu.android.component.gooseberry.webview.hybrid_webview.builder

import android.content.Context
import com.yuu.android.component.gooseberry.webview.hybrid_webview.HybridWebView


/**
 * @ClassName : HybridWebViewBuilder
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/12 18:15
 */

class HybridWebViewBuilder(var context: Context) {

    var isZoomEnable:Boolean?=null
    var isZoomBtnDisplay:Boolean?=null
    var fontSize:Int?=null
    var isDisplayCutoutEnable:Boolean?=null
    var isDayOrNight:Boolean?=null
    var isDebugEnable:Boolean?=null

    fun build(): HybridWebView = HybridWebView(this)
}