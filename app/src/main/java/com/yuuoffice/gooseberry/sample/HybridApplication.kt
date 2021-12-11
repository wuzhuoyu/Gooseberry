package com.yuuoffice.gooseberry.sample

import android.app.Application
import com.yuuoffice.gooseberry.BuildConfig
import com.yuuoffice.hybrid.component.bridge.Bridge
import com.yuuoffice.hybrid.component.config.HybridBridgeConfig


/**
 * @ClassName : HybridApplication
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/13 12:09
 */

class HybridApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Bridge.instance
            .setBridgeConfig(HybridBridgeConfig(scheme = "yuuoffice", host = "host"))
            .openLog(BuildConfig.DEBUG)
            .openEncryption(false)
            .initBridge(this)
    }
}

