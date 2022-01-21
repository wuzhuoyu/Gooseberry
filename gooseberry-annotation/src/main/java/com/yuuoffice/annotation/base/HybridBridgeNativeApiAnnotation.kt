package com.yuuoffice.annotation.base

/**
 * @ClassName : HybridBridgeNativeApiAnnotation
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/20 15:01
 */

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class HybridBridgeNativeApiAnnotation(
    val nativeApi: String,
    val description: String = "javascript call native api"
)
