package com.yuuoffice.annotation.base

fun String.firstCharLowerCase(): String {
    val result = this.toCharArray()
    result[0] = result[0].toLowerCase()
    return result.concatToString()
}

fun String.firstCharUpCase(): String {
    val result = this.toCharArray()
    result[0] = result[0].toUpperCase()
    return result.concatToString()
}