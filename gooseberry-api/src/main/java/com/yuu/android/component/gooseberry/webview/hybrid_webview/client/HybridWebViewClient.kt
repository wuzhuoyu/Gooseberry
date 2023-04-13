package com.yuu.android.component.gooseberry.webview.hybrid_webview.client

import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import com.yuu.android.component.gooseberry.bridge.Bridge
import com.yuu.android.component.gooseberry.ext.errorLog
import com.yuu.android.component.gooseberry.ext.infoLog
import com.yuu.android.component.gooseberry.webview.hybrid_webview.HybridWebView
import java.net.URLDecoder


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
                    hybridWebView.hybridListener?.onNetworkError(error.errorCode,"ERROR_TIMEOUT")
                    errorLog("webview network connection timed out")
                }
                //断网
                ERROR_CONNECT->{
                    hybridWebView.hybridListener?.onNetworkError(error.errorCode,"ERROR_CONNECT")
                    errorLog("webview network disconnect")
                }
                //代理异常
                ERROR_PROXY_AUTHENTICATION->{
                    hybridWebView.hybridListener?.onNetworkError(error.errorCode,"ERROR_PROXY_AUTHENTICATION")
                    errorLog("webview proxy exception")
                }
                // 主机丢失
                ERROR_HOST_LOOKUP->{
                    hybridWebView.hybridListener?.onNetworkError(error.errorCode,"ERROR_HOST_LOOKUP")
                    errorLog("webview host error")
                }
                else->{
                    errorLog("webview other error, message :${error.description}")
                }
            }
        }

        super.onReceivedError(view,request,error)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        infoLog("shouldOverrideUrlLoading request url is $url")

        try {
            // decode 之前，处理 % 和 +
            val replacedUrl = url?.replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
                ?.replace("\\+".toRegex(), "%2B")
            val uri = Uri.parse(URLDecoder.decode(replacedUrl, "utf-8"))
            if (Bridge.instance.processNativeApi(uri)) {
                return true
            }
        } catch (e: Exception) {
            errorLog("解析url报错$e")
        }
        return super.shouldOverrideUrlLoading(view, url)
    }


    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        infoLog("shouldOverrideUrlLoading request url is ${request.url}")
        try {
            // decode 之前，处理 % 和 +
            val replacedUrl = request.url.toString().replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
                .replace("\\+".toRegex(), "%2B")
            val uri = Uri.parse(URLDecoder.decode(replacedUrl,"utf-8"))
            if (Bridge.instance.processNativeApi(uri)){
                return true
            }
        }catch (e: Exception) {
            errorLog("解析url报错$e")
        }

        return super.shouldOverrideUrlLoading(view,request)
    }

    /**
     * 解决部分网站证书问题
     * */
    override fun onReceivedSslError(webView: WebView?, sslErrorHandler: SslErrorHandler?, sslError: SslError?) {
        //super.onReceivedSslError(webView, sslErrorHandler, sslError)注意一定要去除这行代码，否则设置无效。
        // handler.cancel()// Android默认的处理方式
        sslErrorHandler?.proceed()// 接受所有网站的证书
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