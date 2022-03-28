package com.yuu.android.annotation.gooseberry

/**
 * @ClassName : HybridBridgeControllerAnnotation
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/20 15:01
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class HybridBridgeControllerAnnotation(val bridgeControllerName:String, val description:String = "javascript call native controller")
