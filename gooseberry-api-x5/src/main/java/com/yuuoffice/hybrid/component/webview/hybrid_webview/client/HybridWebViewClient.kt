package com.yuuoffice.hybrid.component.webview.hybrid_webview.client

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import com.yuuoffice.hybrid.component.bridge.Bridge
import com.yuuoffice.hybrid.component.ext.errorLog
import com.yuuoffice.hybrid.component.ext.infoLog
import com.yuuoffice.hybrid.component.webview.hybrid_webview.HybridWebView
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


/**
 * @ClassName : HybridWebViewClient
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/17 15:55
 */

class HybridWebViewClient(private val hybridWebView: HybridWebView): WebViewClient() {

    /**
     * 记录上次出现重定向的时间.
     * 避免由于刷新造成循环重定向.
     */
    private var mLastRedirectTime: Long = 0

    /**
     * 默认重定向间隔.
     * 避免由于刷新造成循环重定向.
     */
    private val DEFAULT_REDIRECT_INTERVAL: Long = 3000


    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        infoLog("onPageStarted ${System.currentTimeMillis()}")
        hybridWebView.hybridListener?.onPageStarted(url,favicon)
        super.onPageStarted(view,url,favicon)
    }

    override fun onPageFinished(view: WebView, url: String) {
        infoLog("onPageFinished ${System.currentTimeMillis()}")
        hybridWebView.hybridListener?.onPageFinished(url)

    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //错误重定向循环
            if (error.errorCode == ERROR_REDIRECT_LOOP) {
                //避免由于缓存造成的循环重定向
                resolveRedirect(view)
                return
            }
            when(error.errorCode){
                //网络连接超时
                ERROR_TIMEOUT->{
                    errorLog("webview network connection timed out")
                }
                //断网
                ERROR_CONNECT->{
                    errorLog("webview network disconnect")
                }
                //代理异常
                ERROR_PROXY_AUTHENTICATION->{
                    errorLog("webview proxy exception")
                }
                else->{
                    errorLog("webview other error, message :${error.description}")
                }
            }
            hybridWebView.hybridListener?.onPageError(error.errorCode,error.description.toString(),request.url.toString())
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        infoLog("shouldOverrideUrlLoading request url is $url")

        if (Bridge.instance.processNativeApi(Uri.parse(url))){
            return true
        }
        return super.shouldOverrideUrlLoading(view, url)
    }


    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        infoLog("shouldOverrideUrlLoading request url is ${request.url}")
        if (Bridge.instance.processNativeApi(request.url)){
            return true
        }
        return super.shouldOverrideUrlLoading(view,request)
    }

    /**
     * 解决部分网站证书问题
     * */
    override fun onReceivedSslError(webView: WebView?, sslErrorHandler: SslErrorHandler?, sslError: SslError?) {
        //super.onReceivedSslError(webView, sslErrorHandler, sslError);注意一定要去除这行代码，否则设置无效。
        // handler.cancel();// Android默认的处理方式
        sslErrorHandler?.proceed();// 接受所有网站的证书
    }


    /**
     * 解决重定向
     * @param view  webView
     */
    private fun resolveRedirect(view: WebView) {
        //记录当前时间
        val now = System.currentTimeMillis()
        //mLastRedirectTime 记录上次出现重定向的时间
        if (now - mLastRedirectTime > DEFAULT_REDIRECT_INTERVAL) {
            mLastRedirectTime = System.currentTimeMillis()
            view.reload()
        }
    }
}