package com.yuu.android.component.gooseberry.rsa

import java.security.PublicKey


/**
 * @ClassName : HybridKeyPair
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/10/12 20:32
 */

class HybridPublicKey:PublicKey {
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
