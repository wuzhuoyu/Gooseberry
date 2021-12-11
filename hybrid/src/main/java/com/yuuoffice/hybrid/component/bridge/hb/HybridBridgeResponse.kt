package com.yuuoffice.hybrid.component.bridge.hb


/**
 * @ClassName : HybridResponse
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/10/20 19:57
 */


data class HybridBridgeResponse<T>(

    var returnCode: Int? = null,

    var returnMessage: String? = null,

    val javascriptApi:String? = null,

    var data: T? = null

) {
    companion object {
        const val SUCCESS_CODE = 0
        const val ERROR_CODE = 1
    }
}
