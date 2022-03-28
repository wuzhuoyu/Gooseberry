package com.yuu.android.component.gooseberry.webview.hybrid_webview.client

import android.content.Intent
import android.net.Uri
import android.os.Message
import android.webkit.*
import com.yuu.android.component.gooseberry.ext.debugLog
import com.yuu.android.component.gooseberry.ext.errorLog
import com.yuu.android.component.gooseberry.ext.infoLog
import com.yuu.android.component.gooseberry.ext.warningLog
import com.yuu.android.component.gooseberry.webview.hybrid_webview.HybridWebView
import com.yuu.android.component.gooseberry.bridge.Bridge


/**
 * @ClassName : HybridWebChromeClient
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/17 15:17
 */

class HybridWebChromeClient(private val hybridWebView: HybridWebView) : WebChromeClient() {

    override fun onReceivedTitle(view: WebView, title: String) {
        hybridWebView.hybridListener?.onReceiveTitle(title)
        super.onReceivedTitle(view, title)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        hybridWebView.hybridListener?.onPageProgressed(newProgress)
        super.onProgressChanged(view, newProgress)
    }

    override fun onCreateWindow(
        view: WebView,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message
    ): Boolean {
        val newWebView = HybridWebView(view.context){

        }

        val transport = resultMsg.obj as WebView.WebViewTransport
        transport.webView = newWebView
        resultMsg.sendToTarget()

        newWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse(url)
                view.context.startActivity(browserIntent)
                return true
            }
        }
        return true
    }

    override fun onCloseWindow(window: WebView) {
        infoLog("webview window close")
        super.onCloseWindow(window)
    }

    override fun onConsoleMessage(cm: ConsoleMessage): Boolean {
        when(cm.messageLevel()){
            ConsoleMessage.MessageLevel.LOG->{
                infoLog(cm.message() + "-- From line " + cm.lineNumber() + " of " + cm.sourceId())
            }
            ConsoleMessage.MessageLevel.WARNING->{
                warningLog( cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId())
            }
            ConsoleMessage.MessageLevel.ERROR->{
                errorLog( cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId())
            }
            ConsoleMessage.MessageLevel.DEBUG->{
                debugLog(cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId())
            }
            else -> {
                debugLog( cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId())
            }
        }
        return true
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        infoLog("onJsAlert url:$url message $message ")
        result?.cancel()
        if (Bridge.instance.processNativeApi(Uri.parse(message))){
            return true
        }
        return super.onJsAlert(view,url,message,result)
    }



    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        infoLog("onJsConfirm url:$url message$message ")
        result?.confirm()
        if (Bridge.instance.processNativeApi(Uri.parse(message))){
            return true
        }
        return super.onJsConfirm(view,url,message,result)
    }

    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        infoLog("onJsPrompt url:$url message$message ")
        result?.confirm()
        if (Bridge.instance.processNativeApi(Uri.parse(message))){
            return true
        }
        return super.onJsPrompt(view,url,message,defaultValue,result)
    }
}