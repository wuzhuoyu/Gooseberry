package com.yuu.android.component.gooseberry.rsa

import com.yuu.android.component.gooseberry.ext.base64ToByteArray
import com.yuu.android.component.gooseberry.ext.toBase64
import java.lang.Exception
import java.security.*
import java.security.spec.*
import javax.crypto.Cipher
import kotlin.math.min


/**
 * @ClassName : RSA
 * @Description:
 * @Author: WuZhuoyu
 * @Date: 2021/10/12 11:06
 */

object RSA {
    //全局变量
    private val RSA = "RSA" // 非对称加密密钥算法

    private val ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding" //加密填充方式

    private val DEFAULT_KEY_SIZE = 2048 //秘钥默认长度

    private val DEFAULT_SPLIT = "#PART#".toByteArray() // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密

    private val DEFAULT_BUFFERSIZE = DEFAULT_KEY_SIZE / 8 - 11 // 当前秘钥支持加密的最大字节数


    /**
     * 随机生成RSA密钥对
     * @param keyLength 密钥长度，范围：512～2048  一般1024
     */
    fun generateRSAKeyPair(keyLength: Int = DEFAULT_KEY_SIZE): KeyPair {
        return try {
            val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(RSA)
            kpg.initialize(keyLength)
            kpg.genKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            KeyPair(HybridPublicKey(), HybridPrivateKey())
        }
    }


    /**
     * 公钥加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return String 加密结果->再转换base64
     */
    @Throws(Exception::class)
    fun encryptByPublicKey(data: ByteArray?, publicKey: String): String {
        // 得到公钥
        val keySpec = X509EncodedKeySpec(publicKey.base64ToByteArray())
        val kf: KeyFactory = KeyFactory.getInstance(RSA)
        val keyPublic: PublicKey = kf.generatePublic(keySpec)
        // 加密数据
        val cp: Cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cp.init(Cipher.ENCRYPT_MODE, keyPublic)
        //加密结果转换base64
        return cp.doFinal(data).toBase64()
    }


    /**
     * 私钥加密
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return String 加密结果->再转换base64
     */
    @Throws(Exception::class)
    fun encryptByPrivateKey(data: ByteArray?, privateKey: String): String {
        // 得到私钥
        val keySpec = PKCS8EncodedKeySpec(privateKey.base64ToByteArray())
        val kf: KeyFactory = KeyFactory.getInstance(RSA)
        val keyPrivate: PrivateKey = kf.generatePrivate(keySpec)
        // 数据加密
        val cipher: Cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cipher.init(Cipher.ENCRYPT_MODE, keyPrivate)
        return cipher.doFinal(data).toBase64()
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 公钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPublicKey(data: ByteArray?, publicKey: String): String {
        // 得到公钥
        val keySpec = X509EncodedKeySpec(publicKey.base64ToByteArray())
        val kf: KeyFactory = KeyFactory.getInstance(RSA)
        val keyPublic: PublicKey = kf.generatePublic(keySpec)
        // 数据解密
        val cipher: Cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cipher.init(Cipher.DECRYPT_MODE, keyPublic)
        return String(cipher.doFinal(data))
    }


    /**
     * 私钥解密
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPrivateKey(
        data: ByteArray?,
        privateKey: String
    ): String {
        // 得到私钥
        val keySpec = PKCS8EncodedKeySpec(privateKey.base64ToByteArray())
        val kf: KeyFactory = KeyFactory.getInstance(RSA)
        val keyPrivate: PrivateKey = kf.generatePrivate(keySpec)

        // 解密数据
        val cp: Cipher = Cipher.getInstance(ECB_PKCS1_PADDING)
        cp.init(Cipher.DECRYPT_MODE, keyPrivate)
        return String(cp.doFinal(data))
    }


    /**
     * 公钥分段加密
     * @param data 原文
     * @param publicKey 公钥
     * @return String 加密结果->再转换base64
     */
    @Throws(Exception::class)
    fun encryptByPublicKeyForSpilt(data: ByteArray, publicKey: String): String {
        val dataLen = data.size
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPublicKey(data, publicKey)
        }
        val allBytes: MutableList<Byte> = ArrayList(2048)
        var bufIndex = 0
        var subDataLoop = 0
        var buf: ByteArray? = ByteArray(DEFAULT_BUFFERSIZE)
        for (i in 0 until dataLen) {
            buf!![bufIndex] = data[i]
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++
                if (subDataLoop != 1) {
                    for (b in DEFAULT_SPLIT) {
                        allBytes.add(b)
                    }
                }
                //base64 转换 bytes
                val encryptBytes = encryptByPublicKey(buf, publicKey).base64ToByteArray()
                for (b in encryptBytes) {
                    allBytes.add(b)
                }
                bufIndex = 0
                buf = if (i == dataLen - 1) {
                    null
                } else {
                    ByteArray(min(DEFAULT_BUFFERSIZE, dataLen - i - 1))
                }
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes.toBase64()
    }


    /**
     * 私钥分段加密
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return String 加密结果->再转换base64
     */
    @Throws(Exception::class)
    fun encryptByPrivateKeyForSpilt(data: ByteArray, privateKey: String): String {
        val dataLen = data.size
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPrivateKey(data, privateKey)
        }
        val allBytes: MutableList<Byte> = ArrayList(2048)
        var bufIndex = 0
        var subDataLoop = 0
        var buf: ByteArray? = ByteArray(DEFAULT_BUFFERSIZE)
        for (i in 0 until dataLen) {
            buf!![bufIndex] = data[i]
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++
                if (subDataLoop != 1) {
                    for (b in DEFAULT_SPLIT) {
                        allBytes.add(b)
                    }
                }
                val encryptBytes = encryptByPrivateKey(buf, privateKey).base64ToByteArray()
                for (b in encryptBytes) {
                    allBytes.add(b)
                }
                bufIndex = 0
                buf = if (i == dataLen - 1) {
                    null
                } else {
                    ByteArray(min(DEFAULT_BUFFERSIZE, dataLen - i - 1))
                }
            }
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return bytes.toBase64()
    }

    /**
     * 公钥分段解密
     *
     * @param data 待解密数据
     * @param publicKey 密钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPublicKeyForSpilt(data: ByteArray, publicKey: String): String {
        val splitLen: Int = DEFAULT_SPLIT.size
        if (splitLen <= 0) {
            return decryptByPublicKey(data, publicKey)
        }
        val dataLen = data.size
        val allBytes: MutableList<Byte> = ArrayList(1024)
        var latestStartIndex = 0
        var i = 0
        while (i < dataLen) {
            val bt = data[i]
            var isMatchSplit = false
            if (i == dataLen - 1) {
                // 到data的最后了
                val part = ByteArray(dataLen - latestStartIndex)
                System.arraycopy(data, latestStartIndex, part, 0, part.size)
                val decryptPart: ByteArray = decryptByPublicKey(part, publicKey).toByteArray()
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (j in 1 until splitLen) {
                            if (DEFAULT_SPLIT[j] !== data[i + j]) {
                                break
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true
                }
            }
            if (isMatchSplit) {
                val part = ByteArray(i - latestStartIndex)
                System.arraycopy(data, latestStartIndex, part, 0, part.size)
                val decryptPart: ByteArray = decryptByPublicKey(part, publicKey).toByteArray()
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            }
            i++
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return String(bytes)
    }


    /**
     * 私钥分段解密
     * @param data 待解密原文
     * @param privateKey 私钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPrivateKeyForSpilt(data: ByteArray, privateKey: String): String {
        val splitLen: Int = DEFAULT_SPLIT.size
        if (splitLen <= 0) {
            return decryptByPrivateKey(data, privateKey)
        }
        val dataLen = data.size
        val allBytes: MutableList<Byte> = ArrayList(1024)
        var latestStartIndex = 0
        var i = 0
        while (i < dataLen) {
            val bt = data[i]
            var isMatchSplit = false
            if (i == dataLen - 1) {
                // 到data的最后了
                val part = ByteArray(dataLen - latestStartIndex)
                System.arraycopy(data, latestStartIndex, part, 0, part.size)
                val decryptPart: ByteArray = decryptByPrivateKey(part, privateKey).toByteArray()
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            } else if (bt == DEFAULT_SPLIT[0]) {
                // 这个是以split[0]开头
                if (splitLen > 1) {
                    if (i + splitLen < dataLen) {
                        // 没有超出data的范围
                        for (j in 1 until splitLen) {
                            if (DEFAULT_SPLIT[j] !== data[i + j]) {
                                break
                            }
                            if (j == splitLen - 1) {
                                // 验证到split的最后一位，都没有break，则表明已经确认是split段
                                isMatchSplit = true
                            }
                        }
                    }
                } else {
                    // split只有一位，则已经匹配了
                    isMatchSplit = true
                }
            }
            if (isMatchSplit) {
                val part = ByteArray(i - latestStartIndex)
                System.arraycopy(data, latestStartIndex, part, 0, part.size)
                val decryptPart: ByteArray = decryptByPrivateKey(part, privateKey).toByteArray()
                for (b in decryptPart) {
                    allBytes.add(b)
                }
                latestStartIndex = i + splitLen
                i = latestStartIndex - 1
            }
            i++
        }
        val bytes = ByteArray(allBytes.size)
        run {
            var i = 0
            for (b in allBytes) {
                bytes[i++] = b
            }
        }
        return String(bytes)
    }




    /***************************** 分割线 ********************************/

    /**
     * 公钥加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return String 加密结果->再转换base64
     */
    fun encryptByPublicKey(data: String, publicKey: String): String {
        return encryptByPublicKey(data.toByteArray(),publicKey)
    }


    /**
     * 私钥加密
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return String 加密结果->再转换base64
     */
    @Throws(Exception::class)
    fun encryptByPrivateKey(data: String, privateKey: String): String {
        return encryptByPrivateKey(data.toByteArray(), privateKey)
    }

    /**
     * 公钥解密
     *
     * @param data      待解密数据
     * @param publicKey 公钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPublicKey(data: String, publicKey: String): String {
        return  decryptByPublicKey(data.base64ToByteArray(), publicKey)
    }


    /**
     * 私钥解密
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPrivateKey(data: String, privateKey: String): String {
        return decryptByPrivateKey(data.base64ToByteArray(), privateKey)
    }


    /**
     * 公钥分段加密
     * @param data 原文
     * @param publicKey 公钥
     * @return String 加密结果->再转换base64
     */
    @Throws(Exception::class)
    fun encryptByPublicKeyForSpilt(data: String, publicKey: String): String {
        return encryptByPublicKeyForSpilt(data.toByteArray(), publicKey)
    }


    /**
     * 私钥分段加密
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return String 加密结果->再转换base64
     */
    @Throws(Exception::class)
    fun encryptByPrivateKeyForSpilt(data: String, privateKey: String): String {
        return encryptByPrivateKeyForSpilt(data.toByteArray(), privateKey)
    }

    /**
     * 公钥分段解密
     *
     * @param data 待解密数据
     * @param publicKey 密钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPublicKeyForSpilt(data: String, publicKey: String): String {
        return decryptByPublicKeyForSpilt(data.base64ToByteArray(), publicKey)
    }


    /**
     * 私钥分段解密
     * @param data 待解密原文
     * @param privateKey 私钥
     * @return String 解密数据
     */
    @Throws(Exception::class)
    fun decryptByPrivateKeyForSpilt(data: String, privateKey: String): String {
        return decryptByPrivateKeyForSpilt(data.base64ToByteArray(), privateKey)
    }
}