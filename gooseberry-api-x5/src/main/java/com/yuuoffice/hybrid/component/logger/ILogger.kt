package com.yuuoffice.hybrid.component.logger


/**
 * @InterfaceName : ILogger
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/9/29 21:15
 */

interface ILogger {
    fun showLog(isShowLog: Boolean)
    fun showStackTrace(isShowStackTrace: Boolean)
    fun debug(tag: String?, message: String)
    fun info(tag: String?, message: String)
    fun warning(tag: String?, message: String)
    fun error(tag: String?, message: String)
    fun getDefaultTag(): String?
}
