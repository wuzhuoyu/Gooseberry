package com.yuuoffice.annotation.base


/**
 * @ClassName : BridgeMessage
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/20 15:01
 */

data class HybridBridgeMessage(

    var nativeApi: String = "",

    var javascriptApi: String? = null,

    var data: String? = null
)



