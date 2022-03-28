package com.yuu.gooseberry.sample

import com.yuu.android.component.gooseberry.bridge.Bridge
import com.yuu.android.annotation.gooseberry.HybridBridgeControllerAnnotation
import com.yuu.android.annotation.gooseberry.HybridBridgeMessage
import com.yuu.android.annotation.gooseberry.HybridBridgeNativeApiAnnotation
import com.yuu.android.component.gooseberry.bridge.hb.HybridBridgeResponse


/**
 * @ClassName : TestController
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/27 11:33
 */
@HybridBridgeControllerAnnotation("TestController")
object TestController  {

    @HybridBridgeNativeApiAnnotation(nativeApi = "/ControllerA",description = "执行 ControllerA")
    fun controllerA(hybridBridgeMessage: HybridBridgeMessage){
        val hyMsg = HybridBridgeMessage()
        hyMsg.javascriptApi = hybridBridgeMessage.javascriptApi
        hyMsg.data = "native call js"
        Bridge.instance.processJavascriptApi(HybridBridgeResponse(javascriptApi =hybridBridgeMessage.javascriptApi,data = hyMsg))
    }



}