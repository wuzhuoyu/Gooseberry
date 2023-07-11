package com.yuuoffice.hybrid.component.ext

import android.net.Uri
import android.util.Base64
import com.yuuoffice.hybrid.component.bridge.Bridge


/**
 * @ClassName : StringExt
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/8/27 12:02
 */


/**
 * 没有目标目标字符char 返回空集合
 * */
fun String.accordingToTargetChar(targetChar: Char): List<String> {
    val firstIndex = indexOfFirst { it == targetChar }
    return if (firstIndex != -1) this.split("/").filter { it.isNotEmpty() } else mutableListOf()
}

/**
 * ByteArray 转化  Base64
 * */
fun ByteArray.toBase64(): String = Base64.encodeToString(this,Base64.DEFAULT)


/**
 * Base64 转化  ByteArray
 * */
fun String.base64ToByteArray(): ByteArray = Base64.decode(this,Base64.DEFAULT)


/**
 * 获取hybrid param
 * */
fun Uri.getHybridParam():String?{
    val query = encodedQuery ?: return null
    val firstIndex = query.indexOfFirst { it == '=' }

    return if (firstIndex != -1) query.substring(firstIndex+1,query.length) else ""
}

inline fun debugLog(message:Any){
    Bridge.logger.debug(Bridge.TAG,message.toString())
}

inline fun infoLog(message:Any){
    Bridge.logger.info(Bridge.TAG,message.toString())
}

inline fun warningLog(message:Any){
    Bridge.logger.warning(Bridge.TAG,message.toString())
}

inline fun errorLog(message:Any){
    Bridge.logger.error(Bridge.TAG,message.toString())
}