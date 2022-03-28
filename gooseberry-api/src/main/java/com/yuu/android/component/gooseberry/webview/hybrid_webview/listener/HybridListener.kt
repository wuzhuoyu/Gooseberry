package com.yuu.android.component.gooseberry.webview.hybrid_webview.listener

import android.graphics.Bitmap


/**
 * @ClassName : HybridListener
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/17 15:19
 */

interface HybridListener {

    /**
     * 载入界面调用，如加载进度
     * */
    fun onPageStarted(url: String, favicon: Bitmap?)

    /**
     * 网页的加载进度 & 显示
     * */
    fun onPageProgressed(newProgress: Int)

    /**
     * 页面加载结束时调用
     * */
    fun onPageFinished(url: String)

    /**
     * 页面发生错误的时候调用
     * */
    fun onPageError(errorCode: Int, description: String, failingUrl: String)

    /**
     * 获取web页面的标题
     * */
    fun onReceiveTitle(title: String)

}