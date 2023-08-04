package com.yuu.android.component.gooseberry.bridge.hb

import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.yuu.android.component.gooseberry.config.HybridBridgeConfig
import com.yuu.android.component.gooseberry.ext.errorLog
import com.yuu.android.component.gooseberry.ext.getHybridParam
import com.yuu.android.component.gooseberry.ext.infoLog
import com.yuu.android.component.gooseberry.utils.fromJson
import com.yuu.android.component.gooseberry.utils.toJson
import com.yuu.android.component.gooseberry.webview.hybrid_webview.HybridWebView
import com.yuu.android.annotation.gooseberry.HybridBridgeMessage
import com.yuu.android.annotation.gooseberry.firstCharLowerCase
import com.yuu.android.annotation.gooseberry.firstCharUpCase
import com.yuu.android.component.gooseberry.ext.accordingToTargetChar
import java.net.URLDecoder


/**
 * @ClassName : HybridBridge
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/17 14:45
 */

class HybridBridge : HybridBridgeApi {

    private var bridgeConfig: HybridBridgeConfig? = null

    private  val uiHandler = Handler(Looper.getMainLooper())
    @Synchronized
    override fun init(
        bridgeConfig: HybridBridgeConfig
    ): Boolean {
        this.bridgeConfig = bridgeConfig
        return true
    }

    override fun handlerNativeApi(uri: Uri): Boolean {
        //yuuoffice://host?param={"nativeApi":"TestController/ControllerA","javascriptApi":"TestJSController/ControllerJSA","data":"da#da"}
        infoLog("hybrid bridge communication message is $uri")

        val scheme = uri.scheme
        val host = uri.host

        if (scheme != bridgeConfig?.scheme || host != bridgeConfig?.host) return false

        //解决param带url特殊字符情况
        val temp = uri.toString()
        val result = temp
            .replace("#","%23")
            .replace("&","%26")
            .replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")

        val param = Uri.parse(result).getQueryParameter(bridgeConfig?.param)

        val hybridBridgeMessage: HybridBridgeMessage =param?.fromJson<HybridBridgeMessage>() ?: return false

        // 此处代码设计没得办法
        val apiCollection: List<String> = hybridBridgeMessage.nativeApi.accordingToTargetChar('/')
        if (apiCollection.isEmpty() || apiCollection.size < 2) return false

        val apiControllerName = apiCollection[0].firstCharLowerCase()
        val apiMethodName = apiCollection[1]

        try {
            val helperClass = Class.forName("gooseberry.${apiControllerName.firstCharUpCase()}_Helper")
            val function = helperClass.getMethod(apiMethodName,HybridBridgeMessage::class.java)
            function.invoke(helperClass.kotlin.objectInstance,hybridBridgeMessage)
        }catch (e:Exception){
            errorLog(e.toString())
            return false
        }

        return true
    }


    override fun <T> handlerJavascriptApi(
        hybridWebView: HybridWebView,
        response: HybridBridgeResponse<T>,
        isOpenEncryption: Boolean
    ) {
//@TODO:加密问题
//        if (response.javascriptApi != "rsa_response/get_rsa_public_key"&&response.data!=null&&isOpenEncryption){
//            response.data =  rsaEncryptByPrivateKeyForSpilt(response.data!!.toJson<HybridBridgeResponse<T>>()!!)
//        }

        val messageStr: String = response.toJson<HybridBridgeResponse<T>>()?:""
        val javascriptModel: String = java.lang.String.format(
            EXEC_JS_FORMAT_VALUE,
            "HybridBridge.receiveMessage",
            messageStr
        )
        uiHandler.post {
            hybridWebView.evaluateJavascript(javascriptModel) {}
        }
    }

    companion object {
        const val EXEC_JS_FORMAT_VALUE = "javascript:%s(%s);"
    }
}

