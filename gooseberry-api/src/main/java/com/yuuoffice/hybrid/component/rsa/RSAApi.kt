package com.yuuoffice.hybrid.component.rsa

import java.security.KeyPair


/**
 * @InterfaceName : RSAApi
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/10/12 18:42
 */

interface RSAApi {

    fun getKeyPair(): KeyPair?

    fun getPublicKey(): String

    fun getPrivateKey(): String

}