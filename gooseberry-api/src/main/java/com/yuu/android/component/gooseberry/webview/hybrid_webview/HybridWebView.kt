package com.yuu.android.component.gooseberry.webview.hybrid_webview

import android.content.Context
import com.jeremyliao.liveeventbus.BuildConfig
import com.yuu.android.component.gooseberry.api.HybridWebViewApi
import com.yuu.android.component.gooseberry.bridge.Bridge
import com.yuu.android.component.gooseberry.webview.base_webview.BaseWebView
import com.yuu.android.component.gooseberry.webview.hybrid_webview.builder.HybridWebViewBuilder
import com.yuu.android.component.gooseberry.webview.hybrid_webview.client.HybridWebChromeClient
import com.yuu.android.component.gooseberry.webview.hybrid_webview.client.HybridWebViewClient
import com.yuu.android.component.gooseberry.webview.hybrid_webview.listener.HybridListener


/**
 * @ClassName : HybridWebView
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/12 15:59
 */

class HybridWebView(private val builder: HybridWebViewBuilder) : BaseWebView(builder.context), HybridWebViewApi {

    var hybridListener:HybridListener?=null

    init {
        setWebViewZoomEnable(builder.isZoomEnable?:false)
        setWebViewZoomBtnDisplay(builder.isZoomBtnDisplay?:false)
        setWebViewFontSize(builder.fontSize?:16)
        setWebViewDisplayCutoutEnable(builder.isDisplayCutoutEnable?:true)
        setWebViewDayOrNightModel(builder.isDayOrNight?:true)
        setWebViewDebugEnable(builder.isDebugEnable?: BuildConfig.DEBUG)
        setWebViewJavascriptEnable(true)
        isVerticalFadingEdgeEnabled = false
        isHorizontalFadingEdgeEnabled = false
        settings.apply {
            allowFileAccess = true
            allowContentAccess = true
            domStorageEnabled = true
            databaseEnabled = true
            //支持通过JS打开新窗口
            javaScriptCanOpenWindowsAutomatically = true
        }
        webViewClient = HybridWebViewClient(this)
        webChromeClient = HybridWebChromeClient(this)
        Bridge.instance.bindHybridWebView(this)
    }


    override fun injectHybridListener(listener: HybridListener) {
        this.hybridListener = listener
    }

    override fun destroy() {
        Bridge.instance.unbindHybridWebView()
        loadUrl("about:blank")
        clearFormData()
        clearMatches()
        clearSslPreferences()
        clearDisappearingChildren()
        clearAnimation()
        freeMemory()
        stopLoading()
        // 如果先调用destroy()方法，
        //则会命中if (isDestroyed()) return;需要先onDetachedFromWindow()
        //再 destory()
        //(parent as ViewGroup).removeView(this)
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        settings.javaScriptEnabled = false
        super.destroy()
    }

}

fun HybridWebView(context: Context,block: HybridWebViewBuilder.() -> Unit) = HybridWebViewBuilder(context).apply(block).build()
