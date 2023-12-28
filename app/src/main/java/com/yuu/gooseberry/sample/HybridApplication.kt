package com.yuu.gooseberry.sample

import android.app.Application
import android.util.Log
import com.yuu.gooseberry.BuildConfig
import com.yuu.android.component.gooseberry.bridge.Bridge
import com.yuu.android.component.gooseberry.config.HybridBridgeConfig


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
            .addUploadLogListener {
                Log.d("HolderZone","$it")
            }
            .openEncryption(false)
            .initBridge(this)
    }
}

