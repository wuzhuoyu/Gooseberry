package com.yuuoffice.hybrid.component.bridge

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import com.yuuoffice.hybrid.component.api.BridgeApi
import com.yuuoffice.hybrid.component.rsa.RSAApi
import com.yuuoffice.annotation.base.HybridBridgeControllerAnnotation
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridge
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridgeControllerApi
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridgeResponse
import com.yuuoffice.hybrid.component.config.HybridBridgeConfig
import com.yuuoffice.hybrid.component.event.X5ReadyEvent
import com.yuuoffice.hybrid.component.exception.BridgeException
import com.yuuoffice.hybrid.component.exception.RSAException
import com.yuuoffice.hybrid.component.ext.toBase64
import com.yuuoffice.hybrid.component.logger.DefaultLogger
import com.yuuoffice.hybrid.component.logger.ILogger
import com.yuuoffice.hybrid.component.rsa.RSA
import com.yuuoffice.hybrid.component.utils.HybridPackageUtils
import com.yuuoffice.hybrid.component.webview.hybrid_webview.HybridWebView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.QbSdk.initX5Environment
import com.tencent.smtt.sdk.TbsDownloader
import com.tencent.smtt.sdk.TbsListener
import org.jetbrains.annotations.NotNull
import java.security.KeyPair
import java.util.concurrent.ConcurrentHashMap


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
        initWebViewX5()
        if (isOpenLog){
            logger.showLog(true)
        }
    }

    private fun afterInit() {
        if (isOpenEncryption){
            keyPair = RSA.generateRSAKeyPair()
        }
    }

    private fun initWebViewX5() {

        // 在调用TBS初始化、创建WebView之前进行如下配置，以开启优化方案
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)


        //设置非wifi网络下也可以支持X5下载
        QbSdk.setDownloadWithoutWifi(true)

        //禁止内核初始化完成后创建WebView验证过程，将初始化阶段的验证过程转移到实际使用阶段
        QbSdk.disableAutoCreateX5Webview()

        if (!QbSdk.canLoadX5(mContext)){
            QbSdk.reset(mContext)
            LiveEventBus
                .get(X5ReadyEvent.key, X5ReadyEvent::class.java)
                .post(X5ReadyEvent(false))
        }else{
            LiveEventBus
                .get(X5ReadyEvent.key, X5ReadyEvent::class.java)
                .post(X5ReadyEvent(true))
        }



        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: PreInitCallback = object : PreInitCallback {
            //WebView验证完毕 arg0 是否使用X5内核
            override fun onViewInitFinished(arg0: Boolean) {
                //内核验证失败，进行重新初始化
                if(!arg0){
                    resetX5()
                }else{
                    logger.info(TAG,"X5内核加载成功")
                }
            }
            //内核初始化完毕
            override fun onCoreInitFinished() {
                LiveEventBus
                    .get(X5ReadyEvent.key, X5ReadyEvent::class.java)
                    .post(X5ReadyEvent(true))
                logger.info(TAG,"X5内核初始化完成")
            }
        }


        if(!QbSdk.isTbsCoreInited()){
            initX5Environment(mContext,cb)
        }


        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(p0: Int) {
                if (p0 != 100){
                    resetX5()
                }
            }

            override fun onInstallFinish(p0: Int) {
                if (p0 == 200){
                    logger.info(TAG,"x5下载并安装成功  $p0")
                    QbSdk.preInit(mContext,cb)
                }

            }

            override fun onDownloadProgress(p0: Int) {
                logger.info(TAG,"x5下载进度 $p0"+"   test  ::   "+QbSdk.canLoadX5(mContext).toString())
                if(QbSdk.canLoadX5(mContext)){
                    TbsDownloader.stopDownload()
                }
            }
        })

    }
    private fun resetX5(){
            logger.info(TAG,"X5内核加载失败 重新下载 ")
            QbSdk.reset(mContext)
            // 在调用TBS初始化、创建WebView之前进行如下配置
            val m = HashMap<String, Any>()
            m[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
            m[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
            QbSdk.initTbsSettings(m)
            QbSdk.setDownloadWithoutWifi(true)
            QbSdk.disableAutoCreateX5Webview()
            //验证失败重新下载X5
            TbsDownloader.startDownload(mContext)
    }


    companion object {
        val instance: Bridge by lazy { Bridge() }
        val logger: ILogger by lazy { DefaultLogger() } // 日志工具
        const val TAG = "HybridBridge:"
    }
}