package com.yuuoffice.hybrid.component.ext

import com.yuuoffice.hybrid.component.bridge.Bridge
import com.yuuoffice.hybrid.component.rsa.RSA


/**
 * @ClassName : RsaExt
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/10/15 12:02
 */

/**
 * 私钥加密
 * */
inline fun rsaEncryptByPrivateKey(text:String): String {
    return RSA.encryptByPrivateKey(text,Bridge.instance.getPrivateKey())
}

/**
 * 私钥解密
 * */
inline fun rsaDecryptByPrivateKey(text:String): String {
    return RSA.decryptByPrivateKey(text,Bridge.instance.getPrivateKey())
}

/**
 * 公钥加密
 * */
inline fun rsaEncryptByPublicKey(text:String): String {
    return RSA.encryptByPublicKey(text,Bridge.instance.getPublicKey())
}

/**
 * 公钥解密
 * */
inline fun rsaDecryptByPublicKey(text:String): String {
    return RSA.decryptByPublicKey(text,Bridge.instance.getPublicKey())
}


/**
 * 私钥加密
 * */
inline fun rsaEncryptByPrivateKeyForSpilt(text:String): String {
    return RSA.encryptByPrivateKeyForSpilt(text,Bridge.instance.getPrivateKey())
}

/**
 * 私钥解密
 * */
inline fun rsaDecryptByPrivateKeyForSpilt(text:String): String {
    return RSA.decryptByPrivateKeyForSpilt(text,Bridge.instance.getPrivateKey())
}



/**
 * 公钥加密
 * */
inline fun rsaEncryptByPublicKeyForSpilt(text:String): String {
    return RSA.encryptByPublicKeyForSpilt(text,Bridge.instance.getPublicKey())
}

/**
 * 公钥解密
 * */
inline fun rsaDecryptByPublicKeyForSpilt(text:String): String {
    return RSA.decryptByPublicKeyForSpilt(text,Bridge.instance.getPublicKey())
}
