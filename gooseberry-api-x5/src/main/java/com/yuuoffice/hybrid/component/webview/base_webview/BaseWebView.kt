package com.yuuoffice.hybrid.component.webview.base_webview

import android.annotation.SuppressLint
import android.content.Context
import com.yuuoffice.hybrid.component.exception.WebViewException
import com.yuuoffice.hybrid.component.ext.debugLog
import com.yuuoffice.hybrid.component.webview.base_webview.interfaces.IBaseWebView
import com.tencent.smtt.sdk.*


/**
 * @ClassName : BaseWebView
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/13 12:29
 */

abstract class BaseWebView(context: Context): WebView(context) , IBaseWebView {


    init {
        settings.apply {
            defaultTextEncodingName = "utf-8"
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setWebViewJavascriptEnable(isEnable: Boolean) {
        settings.javaScriptEnabled = isEnabled
    }

    override fun setWebViewZoomEnable(isEnable: Boolean) {
        //支持缩放，默认为true。是下面那个的前提。
        settings.setSupportZoom(isEnable)
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.builtInZoomControls = isEnable
    }

    override fun setWebViewZoomBtnDisplay(isDisplay: Boolean) {
        settings.displayZoomControls = isDisplay
    }

    override fun setWebViewFontSize(fontSize: Int) {
        settings.defaultFontSize = fontSize
    }

    override fun setWebViewDisplayCutoutEnable(isDisplayCutoutEnable: Boolean) {
        // @todo 对于刘海屏机器如果webview被遮挡会自动padding
    }

    override fun setWebViewDayOrNightModel(isDayOrNight: Boolean) {
        // @todo enable:true(日间模式)，enable：false（夜间模式）
    }

    override fun setWebViewDebugEnable(isEnable: Boolean) {
        setWebContentsDebuggingEnabled(isEnable)
    }


    override fun clearWebViewCache() {
        //清除cookie
        CookieManager.getInstance().removeAllCookies(null)
        //清除storage相关缓存
        WebStorage.getInstance().deleteAllData()
        //清除用户密码信息
        WebViewDatabase.getInstance(context).clearUsernamePassword()
        //清除httpauth信息
        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword()
        //清除表单数据
        WebViewDatabase.getInstance(context).clearFormData()
        //清除页面icon图标信息
        WebIconDatabase.getInstance().removeAllIcons()
        //删除地理位置授权，也可以删除某个域名的授权（参考接口类）
        GeolocationPermissions.getInstance().clearAll()
        //清除缓存
        clearCache(true)
        //清除历史
        clearHistory()
    }

    override fun loadLocalUrl(url: String) {
        if (url.isBlank()) throw WebViewException("webview the loaded local url is empty")
        super.loadUrl("file:///android_asset/$url")
    }

    override fun loadRemoteUrl(hostUrl: String, router: String) {
        if (hostUrl.isBlank()) throw WebViewException("webview the loaded host url is empty")
        super.loadUrl(hostUrl+router)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        // @todo 滚动处理
        super.onScrollChanged(l, t, oldl, oldt)
    }
    /*************************************** 以下生命周期处理 *****************************************************/

    override fun onResume() {
        super.onResume()
        debugLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        debugLog("onPause")
    }

    override fun pauseTimers() {
        super.pauseTimers()
        debugLog("pauseTimers")
    }

    override fun resumeTimers() {
        super.resumeTimers()
        debugLog("resumeTimers")
    }

    override fun destroy() {
        stopLoading()
        removeAllViewsInLayout()
        removeAllViews()
        super.destroy()
        debugLog("destroy")
    }
}