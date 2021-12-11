package com.yuuoffice.hybrid.component.logger

import android.text.TextUtils
import android.util.Log


/**
 * @ClassName : DefaultLogger
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/9/29 21:15
 */

class DefaultLogger : ILogger {

    private var defaultTag = "HybridBridge"
    override fun showLog(showLog: Boolean) {
        isShowLog = showLog
    }

    override fun showStackTrace(showStackTrace: Boolean) {
        isShowStackTrace = showStackTrace
    }

    override fun debug(tag: String?, message: String) {
        if (isShowLog) {
            val stackTraceElement = Thread.currentThread().stackTrace[3]
            Log.d(
                if (TextUtils.isEmpty(tag)) getDefaultTag() else tag,
                message + getExtInfo(stackTraceElement)
            )
        }
    }

    override fun info(tag: String?, message: String) {
        if (isShowLog) {
            val stackTraceElement = Thread.currentThread().stackTrace[3]
            Log.i(
                if (TextUtils.isEmpty(tag)) getDefaultTag() else tag,
                message + getExtInfo(stackTraceElement)
            )
        }
    }

    override fun warning(tag: String?, message: String) {
        if (isShowLog) {
            val stackTraceElement = Thread.currentThread().stackTrace[3]
            Log.w(
                if (TextUtils.isEmpty(tag)) getDefaultTag() else tag,
                message + getExtInfo(stackTraceElement)
            )
        }
    }

    override fun error(tag: String?, message: String) {
        if (isShowLog) {
            val stackTraceElement = Thread.currentThread().stackTrace[3]
            Log.e(
                if (TextUtils.isEmpty(tag)) getDefaultTag() else tag,
                message + getExtInfo(stackTraceElement)
            )
        }
    }

    override fun getDefaultTag(): String {
        return defaultTag
    }

    companion object {
        private var isShowLog = false
        private var isShowStackTrace = true
        fun getExtInfo(stackTraceElement: StackTraceElement): String {

            val separator = " \n "
            val sb = StringBuilder()

            if (isShowStackTrace) {
                val threadName = Thread.currentThread().name
                val fileName = stackTraceElement.fileName
                val className = stackTraceElement.className
                val methodName = stackTraceElement.methodName
                val threadID = Thread.currentThread().id
                val lineNumber = stackTraceElement.lineNumber

                sb.append(separator)
                sb.append("[ ")
                sb.append("ThreadId=").append(threadID).append(separator)
                sb.append("ThreadName=").append(threadName).append(separator)
                sb.append("FileName=").append(fileName).append(separator)
                sb.append("ClassName=").append(className).append(separator)
                sb.append("MethodName=").append(methodName).append(separator)
                sb.append("LineNumber=").append(lineNumber)
                sb.append(" ] ")
            }

            return sb.toString()
        }
    }
}