package com.yuu.android.component.gooseberry.config


/**
 * @ClassName : HybridBridgeConfig
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/24 11:03
 */

data class HybridBridgeConfig(
    val scheme: String = SCHEME,
    val host:String = HOST,
    val param:String = PARAM
) {

    companion object {
        //私有协议标记
        const val SCHEME = "scheme"

        //host标记
        const val HOST = "host"

        //消息标记
        const val PARAM = "param"
    }

}