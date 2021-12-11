package com.yuuoffice.hybrid.component.rsa

import java.security.PrivateKey


/**
 * @ClassName : HybridKeyPair
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/10/12 20:32
 */

class HybridPrivateKey:PrivateKey {
    override fun getAlgorithm(): String {
        return "unknown"
    }

    override fun getFormat(): String? {
        return null
    }

    override fun getEncoded(): ByteArray? {
        return null
    }
}
