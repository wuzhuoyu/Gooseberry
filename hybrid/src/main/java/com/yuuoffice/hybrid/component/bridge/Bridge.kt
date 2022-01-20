package com.yuuoffice.hybrid.component.bridge

import android.app.Application
import android.content.Context
import android.net.Uri
import com.yuuoffice.hybrid.component.api.BridgeApi
import com.yuuoffice.hybrid.component.rsa.RSAApi
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridge
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridgeResponse
import com.yuuoffice.hybrid.component.config.HybridBridgeConfig
import com.yuuoffice.hybrid.component.exception.BridgeException
import com.yuuoffice.hybrid.component.exception.RSAException
import com.yuuoffice.hybrid.component.ext.toBase64
import com.yuuoffice.hybrid.component.logger.DefaultLogger
import com.yuuoffice.hybrid.component.logger.ILogger
import com.yuuoffice.hybrid.component.rsa.RSA
import com.yuuoffice.hybrid.component.webview.hybrid_webview.HybridWebView
import java.security.KeyPair


open class Bridge : BridgeApi, RSAApi {

    private lateinit var mContext:Context

    @Volatile
    private var hasInit: Boolean = false

    private var bridgeConfig: HybridBridgeConfig? = null

    private var isOpenLog:Boolean = false

    private var isOpenEncryption:Boolean = false

    private val hybridBridge: HybridBridge by lazy { HybridBridge() }

    /**密钥*/
    private lateinit var keyPair: KeyPair

    /**当前线程的HybridWebView*/
    protected var hybridWebView:HybridWebView?=null

    fun initBridge(context: Application) {
        if (!hasInit) {
            mContext = context
            startInit()
            bridgeConfig = bridgeConfig ?: HybridBridgeConfig()
            hasInit = hybridBridge.init(bridgeConfig!!)
            afterInit()
        }
    }


    override fun setBridgeConfig(hybridBridgeConfig: HybridBridgeConfig): Bridge {
        this.bridgeConfig = hybridBridgeConfig
        return this
    }

    override fun openLog(isOpenLog: Boolean): Bridge {
        this.isOpenLog = isOpenLog
        return this
    }

    override fun openEncryption(isOpenEncryption: Boolean):Bridge {
        this.isOpenEncryption = isOpenEncryption
        return this
    }

    override fun processNativeApi(uri: Uri): Boolean {
        if (hasInit) {
            return hybridBridge.handlerNativeApi(uri)
        } else {
            throw BridgeException(msg = "hybrid bridge not initialized yet")
        }
    }

    override fun <T> processJavascriptApi(response: HybridBridgeResponse<T>) {
        if (hasInit&&hybridWebView!=null) {
            return hybridBridge.handlerJavascriptApi(hybridWebView!!, response,isOpenEncryption)
        } else {
            throw BridgeException(msg = "hybrid bridge not initialized yet")
        }
    }

    override fun bindHybridWebView(hybridWebView: HybridWebView) {
        this.hybridWebView = hybridWebView
    }

    override fun unbindHybridWebView() {
        hybridWebView = null
    }

    override fun destroy() {
        hasInit = false
        isOpenLog = false
        hybridWebView = null
        logger.info(TAG,"Bridge destroy success!")
    }

    override fun getKeyPair(): KeyPair {
        if (!isOpenEncryption) throw RSAException("encryption policy is not turned on")
        return keyPair
    }

    override fun getPublicKey(): String {
        if (!isOpenEncryption) throw RSAException("encryption policy is not turned on")
        return keyPair.public.encoded.toBase64()
    }

    override fun getPrivateKey(): String {
        if (!isOpenEncryption) throw RSAException("encryption policy is not turned on")
        return keyPair.private.encoded.toBase64()
    }

    private fun startInit() {
        if (isOpenLog){
            logger.showLog(true)
        }
    }

    private fun afterInit() {
        if (isOpenEncryption){
            keyPair = RSA.generateRSAKeyPair()
        }
    }

    companion object {
        val instance: Bridge by lazy { Bridge() }
        val logger: ILogger by lazy { DefaultLogger() } // 日志工具
        const val TAG = "HybridBridge:"
    }
}