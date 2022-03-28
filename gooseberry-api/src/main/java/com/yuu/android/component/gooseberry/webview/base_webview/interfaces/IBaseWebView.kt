package com.yuu.android.component.gooseberry.webview.base_webview.interfaces


/**
 * @ClassName : WebViewImpl
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/14 15:29
 */
interface IBaseWebView {

    /**
     * 设置JavaScript是否允许
     * @param isEnable
     * */
    fun setWebViewJavascriptEnable(isEnable: Boolean)

    /**
     * 缩放操作
     * @param isEnable 是否允许缩放
     * */
    fun setWebViewZoomEnable(isEnable: Boolean)

    /**
     * 缩放按钮控制
     * @param isDisplay 是否显示
     * */
    fun setWebViewZoomBtnDisplay(isDisplay: Boolean)

    /**
     * 设置字体大小
     * @param fontSize 字体大小
     */
    fun setWebViewFontSize(fontSize: Int)

    /**
     * 刘海屏适配
     * @param isDisplayCutoutEnable  是否适配
     */
    fun setWebViewDisplayCutoutEnable(isDisplayCutoutEnable: Boolean)

    /**
     * 日间/夜间模式
     * @param isDayOrNight        true(日间模式)
     */
    fun setWebViewDayOrNightModel(isDayOrNight: Boolean)

    /**
     * 设置是否允许抓包
     * APP自身必须调用WebView.setWebContentsDebuggingEnabled(true); 才会允许被DevTools调试
     * @param isEnable            默认允许
     */
    fun setWebViewDebugEnable(isEnable: Boolean)

    /**
     * 缓存清除
     * 针对性删除
     */
    fun clearWebViewCache()

    /**
     * 设置本地html文件为加载url
     * @sample "file:///android_asset/$url"
     * @exception   默认不能为空
     * @param url   加载的路径,不包含 "file:///android_asset/"
     * */
    fun loadLocalUrl(url: String)

    /**
     * 设置远端html文件为加载url
     * @exception hostUrl不可以为空
     * @param hostUrl 主域名
     * @param router 路由
     * */
    fun loadRemoteUrl(hostUrl: String, router: String)
}