package com.yuuoffice.gooseberry.sample

import com.yuuoffice.hybrid.component.bridge.Bridge
import com.yuuoffice.annotation.base.HybridBridgeControllerAnnotation
import com.yuuoffice.annotation.base.HybridBridgeMessage
import com.yuuoffice.annotation.base.HybridBridgeNativeApiAnnotation
import com.yuuoffice.hybrid.component.bridge.hb.HybridBridgeResponse


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