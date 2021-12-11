package com.yuuoffice.gooseberry.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.yuuoffice.annotation.base.HybridBridgeMessage
import com.yuuoffice.gooseberry.R
import com.yuuoffice.hybrid.component.bridge.Bridge
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridgeResponse
import com.yuuoffice.hybrid.component.ext.*
import com.yuuoffice.hybrid.component.webview.hybrid_webview.HybridWebView


class MainHybridActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hybrid)

        val webView: HybridWebView = HybridWebView(this) {
            isDebugEnable = true
            isZoomBtnDisplay = true
            isZoomEnable = true
        }
        findViewById<FrameLayout>(R.id.fl_web).addView(webView)

        webView.loadLocalUrl("hybrid.html")

        findViewById<Button>(R.id.btn_native).setOnClickListener {
            val hyMsg = HybridBridgeMessage()
            hyMsg.javascriptApi = "TestJSController/RegisterListener"
            hyMsg.data = "测试"
            Bridge.instance.processJavascriptApi(HybridBridgeResponse(javascriptApi = "TestJSController/RegisterListener",data = hyMsg))
        }

    }
}
