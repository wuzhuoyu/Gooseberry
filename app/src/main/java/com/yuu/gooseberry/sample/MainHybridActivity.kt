package com.yuu.gooseberry.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.yuu.android.annotation.gooseberry.HybridBridgeMessage
import com.yuu.gooseberry.R
import com.yuu.android.component.gooseberry.bridge.Bridge
import com.yuu.android.component.gooseberry.bridge.hb.HybridBridgeResponse
import com.yuu.android.component.gooseberry.ext.*
import com.yuu.android.component.gooseberry.webview.hybrid_webview.HybridWebView


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
